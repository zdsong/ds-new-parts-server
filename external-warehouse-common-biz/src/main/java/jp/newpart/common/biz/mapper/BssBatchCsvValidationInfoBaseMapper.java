package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;

import java.util.List;


/**
 * バッチ実行CSV検証情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssBatchCsvValidationInfoBaseMapper {

     /**
     *バッチ実行CSV検証情報を検索
     *
     * @param id バッチ実行CSV検証情報プライマリ・キー
     * @return バッチ実行CSV検証情報
     */
    public BssBatchCsvValidationInfoEntity selectById(String id);

     /**
     *バッチ実行CSV検証情報を検索リスト
     *
     * @param bssBatchCsvValidationInfo バッチ実行CSV検証情報
     * @return バッチ実行CSV検証情報リスト
     */
    public List<BssBatchCsvValidationInfoEntity> selectList(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo);

     /**
     *バッチ実行CSV検証情報を登録
     *
     * @param bssBatchCsvValidationInfo バッチ実行CSV検証情報
     * @return  結果
     */
    public int insert(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo);

     /**
     *バッチ実行CSV検証情報を修正
     *
     * @param bssBatchCsvValidationInfo バッチ実行CSV検証情報
     * @return  結果
     */
    public int update(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo);

     /**
     * バッチ実行CSV検証情報を削除
     *
     * @param id バッチ実行CSV検証情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
