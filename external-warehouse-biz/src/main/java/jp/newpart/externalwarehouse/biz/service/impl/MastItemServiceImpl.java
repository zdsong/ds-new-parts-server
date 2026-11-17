package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastItemEntity;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.file.FileUtils;
import jp.newpart.externalwarehouse.biz.constants.BizConstants;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.newpart.externalwarehouse.biz.mapper.MastItemMapper;
import jp.newpart.externalwarehouse.biz.service.IMastItemService;
import jp.newpart.externalwarehouse.biz.util.CsvValidateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 品目マスタ Service業務処理
 *
 * @author nextosd
 */
@Service
public class MastItemServiceImpl implements IMastItemService {

    /**
     * 取込プログラムID
     */
    private static final String PG_ID = "N008";

    /**
     * CSVヘッダー：品目コード
     */
    private static final String CSV_HEADER_ITEM_CODE = "品目コード";

    /**
     * CSVヘッダー：品目名
     */
    private static final String CSV_HEADER_ITEM_NAME = "品目名";

    /**
     * CSVヘッダー：品目名カナ
     */
    private static final String CSV_HEADER_PRPRKN = "品目名カナ";

    /**
     * CSVヘッダー：品名（伝票用）
     */
    private static final String CSV_HEADER_PRPRKJ = "品名（伝票用）";

    /**
     * CSVヘッダー：規格
     */
    private static final String CSV_HEADER_PRKKU = "規格";

    /**
     * CSVヘッダー：品目区分
     */
    private static final String CSV_HEADER_PRHNMX = "品目区分";

    /**
     * CSVヘッダー：備考
     */
    private static final String CSV_HEADER_PRBIKO = "備考";

    @Autowired
    private MastItemMapper mastItemMapper;

    /**
     * 品目コードで品目マスタを検索
     *
     * @param itemCode 品目コード
     * @return 品目マスタ
     */
    @Override
    public MastItemVo selectByItemCode(String itemCode) {
        return mastItemMapper.selectByItemCode(itemCode);
    }

    /**
     * 品目マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 品目マスタリスト
     */
    @Override
    public List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo) {
        return mastItemMapper.selectList(mastItemSearchBo);
    }

    /**
     * 品目情報を取り込む
     *
     * CSV項目：品目コード, 品目名, 品目名カナ, 品名（伝票用）, 規格, 品目区分, 備考
     *
     * @param csvFile CSVファイル
     * @return エラー情報のリスト
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

        // ファイル名チェック
        if (!fileName.startsWith("item")) {
            throw new ServiceException(MessageUtils.message("E0027", "item", "item", fileName));
        }

        // ファイルサイズチェック
        if (csvFile.getSize() > BizConstants.NUMBER_10 * BizConstants.NUMBER_1024 * BizConstants.NUMBER_1024) {
            throw new ServiceException(MessageUtils.message("E0024", "10MB"));
        }
        if (csvFile.getSize() == 0) {
            throw new ServiceException(MessageUtils.message("E0025", fileName));
        }

        // 文字コード判定
        String charsetName;
        if (FileUtils.hasUtf8(csvFile)) {
            charsetName = Constants.UTF8;
        } else if (FileUtils.hasShiftJIS(csvFile)) {
            charsetName = Constants.SHIFT_JIS;
        } else {
            throw new ServiceException(MessageUtils.message("E0026", fileName, Constants.SHIFT_JIS + "/" + Constants.UTF8));
        }

        List<MastItemEntity> importList = new ArrayList<>();
        // 同一ファイル内の品目コード重複チェック用
        Set<String> fileCodeSet = new HashSet<>();

        try (Reader fileReader = new InputStreamReader(csvFile.getInputStream(), Charset.forName(charsetName));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder()
                     .setDelimiter(Constants.COMMA)
                     .setQuote('\"')
                     .setIgnoreSurroundingSpaces(true)
                     .setIgnoreEmptyLines(false)
                     .build())) {

            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (CollectionUtils.isEmpty(csvRecords) || csvRecords.size() <= 1) {
                throw new ServiceException(MessageUtils.message("E0025", fileName));
            }

            // ヘッダー項目の位置を取得する
            Map<String, Integer> headerIndexMap = new HashMap<>();
            CSVRecord headerRecord = csvRecords.get(0);
            for (int index = 0; index < headerRecord.size(); index++) {
                headerIndexMap.put(headerRecord.get(index).replace("\uFEFF", "").trim(), index);
            }

            if (!headerIndexMap.containsKey(CSV_HEADER_ITEM_CODE)
                    || !headerIndexMap.containsKey(CSV_HEADER_ITEM_NAME)) {
                throw new ServiceException(MessageUtils.message("E0026", fileName,
                        CSV_HEADER_ITEM_CODE + "," + CSV_HEADER_ITEM_NAME));
            }

            for (CSVRecord record : csvRecords) {
                long csvRowLine = record.getRecordNumber();
                // 1行目はヘッダーのためスキップする
                if (csvRowLine <= 1) {
                    continue;
                }

                String rowLine = String.valueOf(csvRowLine);
                String itemCode = getCsvValue(record, headerIndexMap, CSV_HEADER_ITEM_CODE);
                String itemName = getCsvValue(record, headerIndexMap, CSV_HEADER_ITEM_NAME);
                String prprkn = getCsvValue(record, headerIndexMap, CSV_HEADER_PRPRKN);
                String prprkj = getCsvValue(record, headerIndexMap, CSV_HEADER_PRPRKJ);
                String prkku = getCsvValue(record, headerIndexMap, CSV_HEADER_PRKKU);
                String prhnmx = getCsvValue(record, headerIndexMap, CSV_HEADER_PRHNMX);
                String prbiko = getCsvValue(record, headerIndexMap, CSV_HEADER_PRBIKO);

                // 空行はスキップする
                if (itemCode.isEmpty() && itemName.isEmpty()) {
                    continue;
                }

                boolean rowError = false;
                if (itemCode.isEmpty()) {
                    CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                            MessageUtils.message("E0003", "品目コード"), errorInfoList);
                    rowError = true;
                }
                if (itemName.isEmpty()) {
                    CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                            MessageUtils.message("E0003", "品目名"), errorInfoList);
                    rowError = true;
                }
                if (!itemCode.isEmpty() && !fileCodeSet.add(itemCode)) {
                    CsvValidateUtil.makeErrorInfo(fileName, rowLine,
                            MessageUtils.message("E0012", "品目コード"), errorInfoList);
                    rowError = true;
                }

                if (rowError) {
                    continue;
                }

                MastItemEntity entity = new MastItemEntity();
                entity.setItemCode(itemCode);
                entity.setItemName(itemName);
                entity.setPrprkn(emptyToNull(prprkn));
                entity.setPrprkj(emptyToNull(prprkj));
                entity.setPrkku(emptyToNull(prkku));
                entity.setPrhnmx(emptyToNull(prhnmx));
                entity.setPrbiko(emptyToNull(prbiko));
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
        for (MastItemEntity entity : importList) {
            MastItemVo existItem = mastItemMapper.selectByItemCode(entity.getItemCode());
            if (existItem == null) {
                entity.setVersion(1);
                entity.setCreateTime(now);
                entity.setCreateBy(username);
                entity.setCreatePgId(PG_ID);
            }
            entity.setUpdateTime(now);
            entity.setUpdateBy(username);
            entity.setUpdatePgId(PG_ID);

            if (existItem == null) {
                mastItemMapper.insert(entity);
            } else {
                mastItemMapper.update(entity);
            }
        }
        return errorInfoList;
    }

    /**
     * CSVレコードから対象項目の値を取得する
     *
     * @param record          CSVレコード
     * @param headerIndexMap  ヘッダー位置
     * @param headerName      項目名
     * @return 項目値
     */
    private String getCsvValue(CSVRecord record, Map<String, Integer> headerIndexMap, String headerName) {
        Integer index = headerIndexMap.get(headerName);
        if (index == null || index < 0 || index >= record.size()) {
            return "";
        }
        return record.get(index).trim();
    }

    /**
     * 空文字をNULLに変換する
     *
     * @param value 値
     * @return 変換後の値
     */
    private String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
