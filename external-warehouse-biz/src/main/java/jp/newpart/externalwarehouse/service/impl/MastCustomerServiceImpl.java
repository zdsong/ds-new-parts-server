package jp.newpart.externalwarehouse.biz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.newpart.common.biz.constants.ExternalWarehouseConstants;
import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;
import jp.newpart.common.biz.domain.entity.MastCustomerEntity;

import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.biz.mapper.BssBatchCsvValidationInfoBaseMapper;
import jp.newpart.common.biz.mapper.MastCustomerBaseMapper;

import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.core.service.IRangedSeqService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.bean.BeanUtils;
import jp.newpart.common.utils.file.FileUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.constants.BizConstants;
import jp.newpart.externalwarehouse.biz.domain.bo.MastCustomerImportBo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastCustomerSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastCustomerVo;
import jp.newpart.externalwarehouse.biz.mapper.MastCustomerMapper;
import jp.newpart.externalwarehouse.biz.service.IMastCustomerService;
import jp.newpart.externalwarehouse.biz.util.CsvValidateUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

/**
 * 顧客マスタ Service業務処理
 *
 * @author nextosd
 */
@Service
@Slf4j
public class MastCustomerServiceImpl implements IMastCustomerService {

    @Autowired
    private MastCustomerMapper mastCustomerMapper;

    @Autowired
    private MastCustomerBaseMapper mastCustomerBaseMapper;

    @Autowired
    private IRangedSeqService rangedSeqService;

    @Autowired
    private BssBatchCsvValidationInfoBaseMapper bssBatchCsvValidationInfoBaseMapper;

    /**
     * 顧客マスタの検索
     *
     * @param id 顧客マスタプライマリ・キー
     * @return 顧客マスタ
     */
    @Override
    public MastCustomerVo selectCustomerById(String id) {
        MastCustomerVo result = mastCustomerMapper.selectCustomerById(id);

        if(ValueUtils.isNotEmpty(result)){
            // 電話番号 復号
            result.setPhoneNumber(DecryptUtils.decryptAES(result.getPhoneNumber()));
            // メールアドレス 復号
            result.setMailAddress(DecryptUtils.decryptAES(result.getMailAddress()));
        }

        return result;
    }

    /**
     * 顧客マスタの検索
     *
     * @param customerId 顧客ID
     * @return 顧客マスタ
     */
    @Override
    public MastCustomerVo selectCustomerByCustomerId(String customerId) {
        return mastCustomerMapper.selectCustomerByCustomerId(customerId, SecurityUtils.getUsername());
    }

    /**
     * 顧客マスタのリストを検索
     *
     * @param mastCustomerSearchBo 顧客マスタパラメータ対象
     * @return 顧客マスタリスト
     */
    @Override
    public List<MastCustomerVo> selectCustomerList(MastCustomerSearchBo mastCustomerSearchBo) {
        mastCustomerSearchBo.setFarmerId(SecurityUtils.getUsername());

        return mastCustomerMapper.selectList(mastCustomerSearchBo);

    }

    /**
     * 顧客マスタ情報を取込む
     *
     * @param csvFile エクセルファイル
     * @return エラー情報リスト
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportFileErrorVo> importCsvData(MultipartFile csvFile) {
        List<ImportFileErrorVo> errorInfoList = new ArrayList<>();
        String fileName = csvFile.getOriginalFilename();

        if (fileName == null || !StringUtils.toCamelCase(fileName).endsWith("csv")) {
            // 拡張子チェック CSVファイルを選択してください。
            throw new ServiceException(MessageUtils.message("E0009", MessageUtils.message("csvExtension")));
        }

        // CSVインポート CSVファイル Charset
        String charsetName = null;

        if(FileUtils.hasUtf8(csvFile)){
            charsetName = Constants.UTF8;
        } else if(FileUtils.hasShiftJIS(csvFile)){
            charsetName = Constants.SHIFT_JIS;
        } else {
            // {0}CSVファイルのフォーマットが不正です。正しいフォーマットのCSVファイルを選択してください。
            throw new ServiceException(MessageUtils.message("E0026", csvFile.getOriginalFilename(), Constants.SHIFT_JIS + "/" + Constants.UTF8));
        }

        long totalSize = csvFile.getSize();

        if (totalSize > BizConstants.NUMBER_10 * BizConstants.NUMBER_1024 * BizConstants.NUMBER_1024) {
            // ファイルのサイズは10MB以下を選択してください。
            throw new ServiceException(MessageUtils.message("E0024", "10MB"));
        }

        // 選択されたファイルのサイズ = 0MB または　CSVにデータが0件の場合
        String csvFileName = csvFile.getOriginalFilename();
        if (!Objects.requireNonNull(csvFileName).startsWith("customer")
                && !Objects.requireNonNull(csvFileName).startsWith("customer")) {
            // アップロード対象が顧客情報の場合、顧客情報以外ファイル(不正のファイル名)を選択しないでください。
            String param = MessageUtils.message("label.customer");
            throw new ServiceException(MessageUtils.message("E0027", param, param, csvFileName));
        }

        if (csvFile.getSize() == 0) {
            // {不正のファイル名}ファイルのサイズが0Mであるか。データを追加した後、再度アップロードしてください。
            throw new ServiceException(MessageUtils.message("E0025", csvFileName));
        }

        BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfoSearch = new BssBatchCsvValidationInfoEntity();
        bssBatchCsvValidationInfoSearch.setCsvFileType("C");
        bssBatchCsvValidationInfoSearch.setCsvImportFlg("1");
        List<BssBatchCsvValidationInfoEntity> bssBatchCsvValidationInfoList = bssBatchCsvValidationInfoBaseMapper.selectList(bssBatchCsvValidationInfoSearch);
        if (CollectionUtils.isEmpty(bssBatchCsvValidationInfoList)) {
            throw new ServiceException("提示バッチ実行CSV検証情報表数据不存在");
        }
        bssBatchCsvValidationInfoList.sort(Comparator.comparingInt(BssBatchCsvValidationInfoEntity::getCsvFileColIdx));
        List<Map<String,Object>> saveList = new ArrayList<>();
        try (Reader fileReader = new InputStreamReader(csvFile.getInputStream(), Charset.forName(charsetName));
             // 区切り文字をカンマに設定
             // 引用文字をダブルクォートに設定
             // エスケープ文字をバックスラッシュに設定を無視
             // フィールド周囲の空白文字を無視
             // 空行を無視しない
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder()
                     .setDelimiter(Constants.COMMA)
                     .setQuote('\"')
                     .setIgnoreSurroundingSpaces(true)
                     .setIgnoreEmptyLines(false)
                     .build());
        ) {

            List<CSVRecord> csvRecords = csvParser.getRecords();

            if (!CollectionUtils.isEmpty(csvRecords) && csvRecords.size() == 1) {
                // {不正のファイル名}ファイルのサイズが0Mであるか。データを追加した後、再度アップロードしてください。
                throw new ServiceException(MessageUtils.message("E0025", csvFileName));
            }

            for (CSVRecord record : csvRecords) {
                // レコードのフィールド数を取得
                // int fieldCount = record.size();
                long csvRowLine = record.getRecordNumber();
                String fileRowLine = ConvertUtils.toStr(csvRowLine);

                if (csvRowLine <= 1) {
                    // 最初の2行をスキップ
                    continue;
                }
                CsvValidateUtil.csvValidateField("C", csvFileName, record, fileRowLine, bssBatchCsvValidationInfoList, errorInfoList, saveList);
            }

        } catch (IOException e) {
            log.error("CSVファイルを読み取る際にエラーが発生しました。", e);
            // I0004=取込に失敗しました。
            throw new ServiceException(MessageUtils.message("I0004"));
        }

        if (!CollectionUtils.isEmpty(errorInfoList)) {
            return errorInfoList;
        }
        for (Map<String,Object> map : saveList) {
            ObjectMapper mapper = new ObjectMapper();
            MastCustomerImportBo sourceEntity = mapper.convertValue(map, MastCustomerImportBo.class);
            MastCustomerEntity mastCustomerEntity = null;
            boolean isUpdate = false;

            MastCustomerEntity query = new MastCustomerEntity();
            // システム顧客番号
            query.setSysCustomerId(sourceEntity.getSysCustomerId());
            // 農家ID
            query.setFarmerId(SecurityUtils.getUsername());
            List<MastCustomerEntity> existCustomerList = mastCustomerBaseMapper.selectList(query);

            // 電話番号が存在する場合は、更新
            if(ValueUtils.isNotEmpty(existCustomerList)){
                mastCustomerEntity = existCustomerList.get(0);
                BeanUtils.copyProperties(sourceEntity, mastCustomerEntity);
                isUpdate = true;
            } else {
                mastCustomerEntity = new MastCustomerEntity();
                BeanUtils.copyProperties(sourceEntity, mastCustomerEntity);
                // ID
                mastCustomerEntity.setId(IdUtils.nextIdStr());
                // Version
                mastCustomerEntity.setVersion(1);
                // 登録プログラムＩＤ
                mastCustomerEntity.setCreatePgId(ExternalWarehousePgIdEnum.N015.getCode());
                // 顧客ID
                String autoCustomerId = rangedSeqService.generateSerialNo(IRangedSeqService.SERIAL_TYPE_CUSTOMER);
                mastCustomerEntity.setCustomerId(autoCustomerId);
            }

            // 顧客名
            mastCustomerEntity.setCustomerName(StringUtils.join(sourceEntity.getFirstName(), ExternalWarehouseConstants.HALF_WIDTH_SPACE, sourceEntity.getLastName()));

            // 住所
            mastCustomerEntity.setAddress(StringUtils.join(sourceEntity.getKenName(), sourceEntity.getCityName(), sourceEntity.getTownName(), sourceEntity.getBuildingEtc()));

            // システムソースフラグ = 1:らくうるカート
            mastCustomerEntity.setSystemSourceFlg("1");

            mastCustomerEntity.setUpdateTime(new Date());
            mastCustomerEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N015.getCode());
            mastCustomerEntity.setUpdateBy(SecurityUtils.getUsername());
            if(isUpdate){
                mastCustomerBaseMapper.update(mastCustomerEntity);
            } else {
                // 農家ID
                mastCustomerEntity.setFarmerId(SecurityUtils.getUsername());
                mastCustomerEntity.setCreateTime(new Date());
                mastCustomerEntity.setCreatePgId(ExternalWarehousePgIdEnum.N015.getCode());
                mastCustomerEntity.setCreateBy(SecurityUtils.getUsername());
                mastCustomerBaseMapper.insert(mastCustomerEntity);
            }
        }
        return errorInfoList;
    }
}
