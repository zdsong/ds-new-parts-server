package jp.newpart.externalwarehouse.biz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;
import jp.newpart.common.biz.domain.entity.MastItemEntity;
import jp.newpart.common.biz.domain.entity.MastItemExtendsEntity;
import jp.newpart.common.biz.domain.entity.MastItemImageEntity;
import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.biz.mapper.BssBatchCsvValidationInfoBaseMapper;
import jp.newpart.common.biz.mapper.MastItemBaseMapper;
import jp.newpart.common.biz.mapper.MastItemExtendsBaseMapper;
import jp.newpart.common.biz.mapper.MastItemImageBaseMapper;
import jp.newpart.common.biz.service.ISysDictTypeService;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.enums.SysDictTypeEnum;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.file.FileUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.constants.BizConstants;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemImportBo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.ImageVo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemImageVo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.newpart.externalwarehouse.biz.mapper.MastItemMapper;
import jp.newpart.externalwarehouse.biz.service.IMastItemService;
import jp.newpart.externalwarehouse.biz.util.CsvValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanUtils;
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
import java.util.regex.Pattern;

/**
 * 商品マスタ Service業務処理
 *
 * @author nextosd
 */
@Service
@Slf4j
public class MastItemServiceImpl implements IMastItemService {

    @Autowired
    private MastItemMapper mastItemMapper;

    @Autowired
    private MastItemBaseMapper mastItemBaseMapper;

    @Autowired
    private MastItemExtendsBaseMapper mastItemExtendsBaseMapper;

    @Autowired
    private MastItemImageBaseMapper mastItemImageBaseMapper;

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired
    private BssBatchCsvValidationInfoBaseMapper bssBatchCsvValidationInfoBaseMapper;

    /**
     * 商品マスタを検索
     *
     * @param itemId 商品マスタプライマリ・キー
     * @return 商品マスタ
     */
    @Override
    public MastItemVo selectByItemId(String itemId) {
        MastItemVo mastItemVo = mastItemMapper.selectByItemId(itemId);
        if(ValueUtils.isNotEmpty(mastItemVo)){
            List<MastItemImageVo> imagesList = mastItemMapper.selectItemImageList(mastItemVo.getId());
            if (!CollectionUtils.isEmpty(imagesList)) {
                MastItemImageVo mastItemImageVo = imagesList.get(0);
                if (ValueUtils.isNotEmpty(mastItemImageVo.getImageUrl())) {
                    mastItemVo.setImageUrls(convertImageVo(mastItemImageVo.getImageUrl()));
                }
                if (ValueUtils.isNotEmpty(mastItemImageVo.getImageOtherUrl())) {
                    mastItemVo.setImageOtherUrls(convertImageVo(mastItemImageVo.getImageOtherUrl()));
                }
                if (ValueUtils.isNotEmpty(mastItemImageVo.getImageListUrl())) {
                    mastItemVo.setImageListUrls(convertImageVo(mastItemImageVo.getImageListUrl()));
                }
            }
            MastItemExtendsEntity mastItemExtends = mastItemExtendsBaseMapper.selectById(itemId);
            if (mastItemExtends != null && ValueUtils.isNotEmpty(mastItemExtends.getOgImageUrl())) {
                mastItemVo.setImagesOgUrls(convertImageVo(mastItemExtends.getOgImageUrl()));
            }
            //mastItemVo.setImages(mastItemMapper.selectItemImageList(mastItemVo.getId()));

            // 梱包まとめタイプ
            if(ValueUtils.isNotEmpty(mastItemVo.getAckagingFlg())){
                mastItemVo.setAskagingFlgLabel(DictUtils.getDictLabel(SysDictTypeEnum.MAST_ACKAGING_TYPE.getCode(), mastItemVo.getAckagingFlg()));
            }

            // 荷扱いタイプ1
            if(ValueUtils.isNotEmpty(mastItemVo.getHandlingType1Flg())){
                mastItemVo.setHandlingType1FlgLabel(DictUtils.getDictLabel(SysDictTypeEnum.MAST_HANDLING_TYPE.getCode(), mastItemVo.getHandlingType1Flg()));
            }

            // 荷扱いタイプ2
            if(ValueUtils.isNotEmpty(mastItemVo.getHandlingType2Flg())){
                mastItemVo.setHandlingType2FlgLabel(DictUtils.getDictLabel(SysDictTypeEnum.MAST_HANDLING_TYPE.getCode(), mastItemVo.getHandlingType2Flg()));
            }

            // 宅急便サイズ
            if(ValueUtils.isNotEmpty(mastItemVo.getCourierSizeFlg())){
                mastItemVo.setCourierSizeFlgLabel(DictUtils.getDictLabel(SysDictTypeEnum.MAST_COURIER_SIZE_TYPE.getCode(), mastItemVo.getCourierSizeFlg()));
            }

            // 便種タイプ
            if(ValueUtils.isNotEmpty(mastItemVo.getShippingFlg())){
                mastItemVo.setShippingFlgLabel(DictUtils.getDictLabel(SysDictTypeEnum.MAST_SHIPPING_TYPE.getCode(), mastItemVo.getShippingFlg()));
            }
        }
        return mastItemVo;
    }

    /**
     * 商品マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 商品マスタリスト
     */
    @Override
    public List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo) {
        // 農家ID（ログインユーザー名）による絞り込みは行わない。
        // 商品マスタは旧システムのCSV連携で登録されており、farmer_id は旧農家IDのまま保持されているため。
        return mastItemMapper.selectList(mastItemSearchBo);
    }

    /**
     * 商品情報を取り込む
     *
     * @param csvFile エクセルファイル
     * @return エラー情報のリスト
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ImportFileErrorVo> importCsvData(MultipartFile csvFile) {

        List<ImportFileErrorVo> errorInfoList = new ArrayList<>();
        String fileName = csvFile.getOriginalFilename();
        String farmerId = SecurityUtils.getLoginUser().getUsername();
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

        // ヘッダー情報と明細情報のエラー情報を格納するリスト
        List<ImportFileErrorVo> errorResultDetailInfoList = new ArrayList<>();
        List<MastItemImportBo> importDataList = new ArrayList<>();

        // 選択されたファイルのサイズ = 0MB または　CSVにデータが0件の場合
        String csvFileName = csvFile.getOriginalFilename();
        if (!Objects.requireNonNull(csvFileName).startsWith("item")
                && !Objects.requireNonNull(csvFileName).startsWith("item")) {
            // アップロード対象が顧客情報の場合、顧客情報以外ファイル(不正のファイル名)を選択しないでください。
            String param = MessageUtils.message("item");
            throw new ServiceException(MessageUtils.message("E0027", param, param, csvFileName));
        }

        if (csvFile.getSize() == 0) {
            // {不正のファイル名}ファイルのサイズが0Mであるか。データを追加した後、再度アップロードしてください。
            throw new ServiceException(MessageUtils.message("E0025", csvFileName));
        }

        BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfoSearch = new BssBatchCsvValidationInfoEntity();
        bssBatchCsvValidationInfoSearch.setCsvFileType("I");
        bssBatchCsvValidationInfoSearch.setCsvImportFlg("1");
        List<BssBatchCsvValidationInfoEntity> bssBatchCsvValidationInfoList = bssBatchCsvValidationInfoBaseMapper.selectList(bssBatchCsvValidationInfoSearch);
        if (CollectionUtils.isEmpty(bssBatchCsvValidationInfoList)) {
            throw new ServiceException("提示バッチ実行CSV検証情報表数据不存在");
        }
        bssBatchCsvValidationInfoList.sort(Comparator.comparingInt(BssBatchCsvValidationInfoEntity::getCsvFileColIdx));
        List<Map<String,Object>> saveList = new ArrayList<>();
        //Map<String,List<Map<String,Object>>> saveMap = new HashMap<>();
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
                if (ValueUtils.isNotEmpty(record) && ValueUtils.isNotEmpty(record.get(0))) {
                    String sysItemCode = record.get(0);
                    MastItemEntity query = new MastItemEntity();
                    query.setSysItemCode(sysItemCode);
                    List<MastItemEntity> existItemList = mastItemBaseMapper.selectList(query);
                    if (!CollectionUtils.isEmpty(existItemList)) {
                        MastItemEntity mastItemEntity = existItemList.get(0);
                        if (!farmerId.equals(mastItemEntity.getFarmerId())) {
                            CsvValidateUtil.makeErrorInfo(csvFileName, fileRowLine, MessageUtils.message("E0067", sysItemCode), errorInfoList);
                        }
                    }
                }
               CsvValidateUtil.csvValidateField("I", csvFileName, record, fileRowLine, bssBatchCsvValidationInfoList, errorInfoList, saveList);
                // Item CSV head column
                // システム商品番号	有効無効フラグ	商品名	バリエーション名	商品コード	JANコード
                // 販売価格	通常価格	会員価格	在庫管理フラグ	残在庫	更新後在庫	最低基準在庫	画像URL
                // カテゴリ番号	キャンペーン番号	消費税	メーカー	ブランド	従量ポイント	個別送料	便種	梱包まとめ
                // 配送方法除外番号	お支払い方法除外番号	個別付与ポイント	同時表示グループ番号	注文年齢制限（下限）
                // 注文年齢制限（上限）	注文数制限（下限）	注文数制限（上限）	販売期間（下限）	販売期間（上限）
                // ロケーション	商品ページ公開フラグ	商品バッジ番号	他画像URL	数量入力フォーム	数値入力選択肢
                // バリエーションパターン１表示	商品情報１	商品情報２	商品情報３	一覧用表示画像URL	title	meta-description
                // og:title	og:type	og:imageURL	新着商品掲載フラグ	バリエーションパターン	バリエーションパターン２項目	ロッカー受け取りサイズ
                // 予約お届け期間番号	クーポン利用除外フラグ	宅急便サイズ	税率区分	販売時間指定	販売時間（下限）	販売時間（上限）	発荷主コード
                // 優先順位	荷扱い	返品区分番号	注文不可指定	定期購入商品設定	定期購入価格（初回価格）	定期購入価格（2回目以降価格）
                // 定期購入回数	定期購入専用フラグ	定期購入商品説明	スマレジ商品ID	お気に入りリスト表示フラグ	入荷通知メールフラグ
            }

        } catch (IOException e) {
            log.error("CSVファイルを読み取る際にエラーが発生しました。", e);
            // I0004=取込に失敗しました。
            throw new ServiceException(MessageUtils.message("I0004"));
        }

        if (!CollectionUtils.isEmpty(errorInfoList)) {
            return errorInfoList;
        }


        // 定期購入専用フラグ
        List<SysDictData> sysDictData_SYS_INVALID_VALID_FLG = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.SYS_INVALID_VALID_FLG.getCode());
        // 便種
        List<SysDictData> sysDictData_MAST_SHIPPING_TYPE = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_SHIPPING_TYPE.getCode());
        // 梱包まとめ
        List<SysDictData> sysDictData_MAST_ACKAGING_TYPE = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_ACKAGING_TYPE.getCode());
        // 非表示/表示フラグ
        List<SysDictData> sysDictData_SYS_NDICATOR_FLG = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.SYS_NDICATOR_FLG.getCode());
        // クーポン利用除外フラグ
        List<SysDictData> sysDictData_SYS_EXCLUSION_FLG = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.SYS_EXCLUSION_FLG.getCode());
        // 宅急便サイズ
        List<SysDictData> sysDictData_MAST_COURIER_SIZE_TYPE = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_COURIER_SIZE_TYPE.getCode());
        // 荷扱い
        List<SysDictData> sysDictData_MAST_HANDLING_TYPE = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_HANDLING_TYPE.getCode());
        // 注文不可指定
        List<SysDictData> sysDictData_MAST_ORDER_PROHIBIT_SPEC_FLG = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_ORDER_PROHIBIT_SPEC_FLG.getCode());
        // 定期購入専用フラグ
        List<SysDictData> sysDictData_MAST_SUB_ONLY_FLG = sysDictTypeService.selectDictDataByType(SysDictTypeEnum.MAST_SUB_ONLY_FLG.getCode());

        for (Map<String,Object> map : saveList) {
            ObjectMapper mapper = new ObjectMapper();
            MastItemImportBo sourceEntity = mapper.convertValue(map, MastItemImportBo.class);
            boolean isUpdate = false;
            boolean isUpdateItemExtends = false;
            MastItemEntity mastItemEntity = null;
            MastItemEntity query = new MastItemEntity();
            String sysItemCode = ValueUtils.isNotEmpty(sourceEntity.getSysItemCode()) ? sourceEntity.getSysItemCode() : "";
           // query.setFarmerId(farmerId);
            query.setSysItemCode(sysItemCode);
            List<MastItemEntity> existItemList = mastItemBaseMapper.selectList(query);
            if(ValueUtils.isNotEmpty(existItemList)){
                mastItemEntity = existItemList.get(0);
                BeanUtils.copyProperties(sourceEntity, mastItemEntity);
                isUpdate = true;
            } else {
                mastItemEntity = new MastItemEntity();
                BeanUtils.copyProperties(sourceEntity, mastItemEntity);
                // Item Id
                mastItemEntity.setItemId(sysItemCode);
                // ID
                mastItemEntity.setId(IdUtils.nextIdStr());
                // Version
                mastItemEntity.setVersion(1);
                // 登録プログラムＩＤ
                mastItemEntity.setCreatePgId(ExternalWarehousePgIdEnum.N008.getCode());
            }
            // 農家ID
            mastItemEntity.setFarmerId(farmerId);
            // 更新プログラムＩＤ
            mastItemEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N008.getCode());
            // 便種
            if (ValueUtils.isNotEmpty(sourceEntity.getShippingFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_MAST_SHIPPING_TYPE)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_SHIPPING_TYPE.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getShippingFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemEntity.setShippingFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemEntity.setShippingFlg("");
                    }
                } else {
                    mastItemEntity.setShippingFlg("");
                }
            }
            // 梱包まとめ
            if (ValueUtils.isNotEmpty(sourceEntity.getAckagingFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_MAST_ACKAGING_TYPE)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_ACKAGING_TYPE.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getAckagingFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemEntity.setAckagingFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemEntity.setAckagingFlg("");
                    }
                } else {
                    mastItemEntity.setAckagingFlg("");
                }
            }
            // 商品ページ公開フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getItemPagePublicFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_NDICATOR_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_NDICATOR_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getItemPagePublicFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemEntity.setItemPagePublicFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemEntity.setItemPagePublicFlg("");
                    }
                } else {
                    mastItemEntity.setItemPagePublicFlg("");
                }
            }
            // 新着商品掲載フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getCourierSizeFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_MAST_COURIER_SIZE_TYPE)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_COURIER_SIZE_TYPE.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getCourierSizeFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemEntity.setCourierSizeFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemEntity.setCourierSizeFlg("");
                    }
                } else {
                    mastItemEntity.setCourierSizeFlg("");
                }
            }
            // 荷扱い
            if(ValueUtils.isNotEmpty(sourceEntity.getHandlingType1Flg())){
                String[] handlingTypeLabelArr = this.handlingSplit(sourceEntity.getHandlingType1Flg());
                // 荷扱い1
                String handlingType1Label = handlingTypeLabelArr[0];
                // 荷扱い2
                String handlingType2Label = handlingTypeLabelArr[1];
                if(ValueUtils.isNotEmpty(sysDictData_MAST_HANDLING_TYPE)){
                    // 荷扱い1
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_HANDLING_TYPE.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), handlingType1Label)).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemEntity.setHandlingType1Flg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemEntity.setHandlingType1Flg("");
                    }
                    // 荷扱い2
                    if(ValueUtils.isNotEmpty(handlingType2Label)){
                        sysDictDataOptional = sysDictData_MAST_HANDLING_TYPE.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), handlingType2Label)).findFirst();
                        if(sysDictDataOptional.isPresent()){
                            mastItemEntity.setHandlingType2Flg(sysDictDataOptional.get().getDictValue());
                        } else {
                            mastItemEntity.setHandlingType2Flg("");
                        }
                    }
                } else {
                    mastItemEntity.setHandlingType1Flg("");
                    mastItemEntity.setHandlingType2Flg("");
                }
            }
            if (isUpdate) {
                mastItemBaseMapper.update(mastItemEntity);
                mastItemMapper.deleteItemImageByItemId(mastItemEntity.getId());
            } else {
                mastItemBaseMapper.insert(mastItemEntity);
            }

            MastItemExtendsEntity mastItemExtendsEntity = mastItemExtendsBaseMapper.selectById(sysItemCode);
            if (ValueUtils.isEmpty(mastItemExtendsEntity)) {
                mastItemExtendsEntity = new MastItemExtendsEntity();
                BeanUtils.copyProperties(sourceEntity, mastItemExtendsEntity);
                mastItemExtendsEntity.setItemId(sysItemCode);
                mastItemExtendsEntity.setCreateTime(new Date());
                mastItemExtendsEntity.setCreatePgId(ExternalWarehousePgIdEnum.N008.getCode());
                mastItemExtendsEntity.setCreateBy(farmerId);
                mastItemExtendsEntity.setUpdateTime(new Date());
                mastItemExtendsEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N008.getCode());
                mastItemExtendsEntity.setUpdateBy(farmerId);
            } else {
                // source => target
                isUpdateItemExtends = true;
                BeanUtils.copyProperties(sourceEntity, mastItemExtendsEntity);
                mastItemExtendsEntity.setItemId(sysItemCode);
                mastItemExtendsEntity.setUpdateTime(new Date());
                mastItemExtendsEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N008.getCode());
                mastItemExtendsEntity.setUpdateBy(farmerId);
            }

            // 在庫管理フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getInvMgmtFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_INVALID_VALID_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_INVALID_VALID_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getInvMgmtFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setInvMgmtFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setInvMgmtFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setInvMgmtFlg("");
                }
            }
            // 新着商品掲載フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getNewProdListFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_NDICATOR_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_NDICATOR_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getNewProdListFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setNewProdListFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setNewProdListFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setNewProdListFlg("");
                }
            }
            // クーポン利用除外フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getCouponExclFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_EXCLUSION_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_EXCLUSION_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getCouponExclFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setCouponExclFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setCouponExclFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setCouponExclFlg("");
                }
            }
            // 注文不可指定
            if (ValueUtils.isNotEmpty(sourceEntity.getOrderProhibitSpecFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_MAST_ORDER_PROHIBIT_SPEC_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_ORDER_PROHIBIT_SPEC_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getOrderProhibitSpecFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setOrderProhibitSpecFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setOrderProhibitSpecFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setOrderProhibitSpecFlg("");
                }
            }
            // 定期購入商品設定
            if (ValueUtils.isNotEmpty(sourceEntity.getSubProdSetting())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_INVALID_VALID_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_INVALID_VALID_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getSubProdSetting())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setSubProdSetting(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setSubProdSetting("");
                    }
                } else {
                    mastItemExtendsEntity.setSubProdSetting("");
                }
            }
            // 定期購入専用フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getSubOnlyFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_MAST_SUB_ONLY_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_MAST_SUB_ONLY_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getSubOnlyFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setSubOnlyFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setSubOnlyFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setSubOnlyFlg("");
                }
            }
            // お気に入りリスト表示フラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getFavListDispFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_INVALID_VALID_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_INVALID_VALID_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getFavListDispFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setFavListDispFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setFavListDispFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setFavListDispFlg("");
                }
            }
            // 入荷通知メールフラグ
            if (ValueUtils.isNotEmpty(sourceEntity.getRestockNotifyFlg())) {
                if (ValueUtils.isNotEmpty(sysDictData_SYS_INVALID_VALID_FLG)) {
                    Optional<SysDictData> sysDictDataOptional = sysDictData_SYS_INVALID_VALID_FLG.stream().filter(m -> ValueUtils.isEqual(m.getDictLabel(), sourceEntity.getRestockNotifyFlg())).findFirst();
                    if(sysDictDataOptional.isPresent()){
                        mastItemExtendsEntity.setRestockNotifyFlg(sysDictDataOptional.get().getDictValue());
                    } else {
                        mastItemExtendsEntity.setRestockNotifyFlg("");
                    }
                } else {
                    mastItemExtendsEntity.setRestockNotifyFlg("");
                }
            }


            if (isUpdateItemExtends) {
                mastItemExtendsBaseMapper.update(mastItemExtendsEntity);
            } else {
                mastItemExtendsBaseMapper.insert(mastItemExtendsEntity);
            }
            if(ValueUtils.isNotEmpty(sourceEntity.getImageUrl()) || ValueUtils.isNotEmpty(sourceEntity.getImageOtherUrl())) {
                MastItemImageEntity mastItemImageEntity = new MastItemImageEntity();
                mastItemImageEntity.setId(IdUtils.nextIdStr());
                mastItemImageEntity.setImageUrl(sourceEntity.getImageUrl());
                mastItemImageEntity.setImageOtherUrl(sourceEntity.getImageOtherUrl());
                mastItemImageEntity.setVersion(1);
                mastItemImageEntity.setItemId(mastItemEntity.getId());
                mastItemImageEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N008.getCode());
                mastItemImageEntity.setCreatePgId(ExternalWarehousePgIdEnum.N008.getCode());
                mastItemImageBaseMapper.insert(mastItemImageEntity);
            }
        }

        return errorInfoList;
    }

    /**
     * 荷扱い => 荷扱い1 & 荷扱い2
     * ナマモノ|天地無用
     * ナマモノ
     * @param handlingType
     * @return
     */
    private String[] handlingSplit(String handlingType){
        int index = handlingType.indexOf("|");
        if(index > 0){
            String[] handllingArr = new String[2];
            handllingArr[0] = handlingType.substring(0, index);
            handllingArr[1] = handlingType.substring(index + 1, handlingType.length());
            return handllingArr;
        } else {
            String[] handllingArr = new String[2];
            handllingArr[0] = handlingType;
            handllingArr[1] = null;
            return handllingArr;
        }
    }

    private List<ImageVo> convertImageVo(String imageUrl) {
        if (ValueUtils.isEmpty(imageUrl)) {
            return null;
        }
        String[] urls = imageUrl.split(Pattern.quote("|"));
        List<ImageVo> result = new ArrayList<>();
        for (String url : urls) {
            ImageVo imageVo = new ImageVo();
            imageVo.setImageUrl(url);
            result.add(imageVo);
        }
        return result;
    }
}
