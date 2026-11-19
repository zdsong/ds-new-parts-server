package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.domain.entity.MastLocationEntity;
import jp.nasta.pms.common.biz.domain.entity.MastWarehouseEntity;
import jp.nasta.pms.common.constant.Constants;
import jp.nasta.pms.common.core.domain.ImportFileErrorVo;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.file.FileUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.constants.BizConstants;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastLocationSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastLocationVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.MastLocationMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IMastLocationService;
import jp.nasta.pms.externalwarehouse.biz.util.CsvValidateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ロケーションマスタ Service業務処理
 *
 * @author nextosd
 */
@Service
public class MastLocationServiceImpl implements IMastLocationService {

    @Autowired
    private MastLocationMapper mastLocationMapper;

    /**
     * ロケーションマスタを検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return ロケーションマスタリスト
     */
    @Override
    public List<MastLocationVo> selectList(MastLocationSearchBo searchBo) {
        return mastLocationMapper.selectList(searchBo);
    }

    /**
     * ロケーションマスタを検索
     *
     * @param id ロケーションマスタプライマリ・キー
     * @return ロケーションマスタ
     */
    @Override
    public MastLocationVo selectById(String id) {
        return mastLocationMapper.selectById(id);
    }

    /**
     * 同一の倉庫コード・ロケーションコードが存在するか確認する
     *
     * @param whCd      倉庫コード
     * @param locCd     ロケーションコード
     * @param excludeId 除外するロケーションマスタID（更新時）
     * @return true:重複なし / false:重複あり
     */
    @Override
    public boolean checkLocationUnique(String whCd, String locCd, String excludeId) {
        return mastLocationMapper.countByWhCdAndLocCd(whCd, locCd, excludeId) == 0;
    }

    /**
     * ロケーションマスタを登録
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    @Override
    public int insertLocation(MastLocationEntity entity) {
        return mastLocationMapper.insert(entity);
    }

    /**
     * ロケーションマスタを修正
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    @Override
    public int updateLocation(MastLocationEntity entity) {
        return mastLocationMapper.update(entity);
    }

    /**
     * ロケーションマスタを論理削除
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    @Override
    public int deleteLocation(MastLocationEntity entity) {
        return mastLocationMapper.deleteLogical(entity);
    }

    /**
     * CSVインポートを実行する
     *
     * CSV項目：倉庫名, ロケ番, コメント
     *
     * @param csvFile アップロードされたCSVファイル
     * @return エラー情報リスト（エラーがない場合は空）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportFileErrorVo> importCsvData(MultipartFile csvFile) {
        List<ImportFileErrorVo> errorInfoList = new ArrayList<>();
        String fileName = csvFile.getOriginalFilename();

        // 拡張子チェック
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new ServiceException(MessageUtils.message("E0009", "CSV"));
        }

        // CSVファイルの文字コードを判定する
        String charsetName;
        if (FileUtils.hasUtf8(csvFile)) {
            charsetName = Constants.UTF8;
        } else if (FileUtils.hasShiftJIS(csvFile)) {
            charsetName = Constants.SHIFT_JIS;
        } else {
            throw new ServiceException(MessageUtils.message("E0026", fileName,
                    Constants.SHIFT_JIS + "/" + Constants.UTF8));
        }

        // ファイルサイズチェック
        if (csvFile.getSize() > BizConstants.NUMBER_10 * BizConstants.NUMBER_1024 * BizConstants.NUMBER_1024) {
            throw new ServiceException(MessageUtils.message("E0024", "10MB"));
        }
        if (csvFile.getSize() == 0) {
            throw new ServiceException(MessageUtils.message("E0025", fileName));
        }

        List<MastLocationEntity> importList = new ArrayList<>();
        // 同一ファイル内の重複チェック用キー
        Set<String> fileKeySet = new HashSet<>();

        try (Reader fileReader = new InputStreamReader(csvFile.getInputStream(), Charset.forName(charsetName));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder()
                     .setDelimiter(Constants.COMMA)
                     .setQuote('\"')
                     .setIgnoreSurroundingSpaces(true)
                     .setIgnoreEmptyLines(false)
                     .build())) {

            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (csvRecords.size() <= 1) {
                throw new ServiceException(MessageUtils.message("E0025", fileName));
            }

            for (CSVRecord record : csvRecords) {
                long csvRowLine = record.getRecordNumber();
                // 1行目はヘッダーのためスキップする
                if (csvRowLine <= 1) {
                    continue;
                }

                String rowLine = String.valueOf(csvRowLine);
                String whNm = record.size() > 0 ? record.get(0).trim() : "";
                String locCd = record.size() > 1 ? record.get(1).trim() : "";
                String memo = record.size() > 2 ? record.get(2).trim() : "";

                // 空行はスキップする
                if (whNm.isEmpty() && locCd.isEmpty()) {
                    continue;
                }

                boolean rowError = false;
                if (whNm.isEmpty()) {
                    CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                            MessageUtils.message("E0003", MessageUtils.message("label.warehouse")), errorInfoList);
                    rowError = true;
                }
                if (locCd.isEmpty()) {
                    CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                            MessageUtils.message("E0003", MessageUtils.message("label.locationCd")), errorInfoList);
                    rowError = true;
                }

                MastWarehouseEntity warehouse = null;
                if (!whNm.isEmpty()) {
                    warehouse = mastLocationMapper.selectWarehouseByNm(whNm);
                    if (warehouse == null) {
                        CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                                MessageUtils.message("E0016", "倉庫「" + whNm + "」"), errorInfoList);
                        rowError = true;
                    }
                }

                // 同一倉庫内でロケ番が重複していないか確認する
                if (warehouse != null && !locCd.isEmpty()) {
                    String fileKey = warehouse.getWhCd() + "\u0000" + locCd;
                    if (fileKeySet.contains(fileKey)
                            || mastLocationMapper.countByWhCdAndLocCd(warehouse.getWhCd(), locCd, null) > 0) {
                        CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                                MessageUtils.message("E0012", MessageUtils.message("label.locationCd")), errorInfoList);
                        rowError = true;
                    } else {
                        fileKeySet.add(fileKey);
                    }
                }

                if (rowError) {
                    continue;
                }

                MastLocationEntity entity = new MastLocationEntity();
                entity.setWhCd(warehouse.getWhCd());
                entity.setLocCd(locCd);
                entity.setMemo(memo.isEmpty() ? null : memo);
                importList.add(entity);
            }
        } catch (IOException e) {
            throw new ServiceException(MessageUtils.message("I0004"));
        }

        // エラーが1件でもある場合は登録しない
        if (!errorInfoList.isEmpty()) {
            return errorInfoList;
        }

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();
        for (MastLocationEntity entity : importList) {
            entity.setId(IdUtils.nextIdStr());
            entity.setDeleteFlg("0");
            entity.setVersion(1);
            entity.setCreateTime(now);
            entity.setCreateBy(username);
            entity.setCreatePgId("P601");
            entity.setUpdateTime(now);
            entity.setUpdateBy(username);
            entity.setUpdatePgId("P601");
            mastLocationMapper.insert(entity);
        }
        return errorInfoList;
    }
}
