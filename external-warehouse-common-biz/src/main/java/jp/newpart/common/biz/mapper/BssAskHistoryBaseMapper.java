package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskHistoryEntity;

import java.util.List;


/**
 * 問合せ対応履歴情報  Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskHistoryBaseMapper {

     /**
     *問合せ対応履歴情報 を検索
     *
     * @param id 問合せ対応履歴情報 プライマリ・キー
     * @return 問合せ対応履歴情報 
     */
    public BssAskHistoryEntity selectById(String id);

     /**
     *問合せ対応履歴情報 を検索リスト
     *
     * @param bssAskHistory 問合せ対応履歴情報 
     * @return 問合せ対応履歴情報 リスト
     */
    public List<BssAskHistoryEntity> selectList(BssAskHistoryEntity bssAskHistory);

     /**
     *問合せ対応履歴情報 を登録
     *
     * @param bssAskHistory 問合せ対応履歴情報 
     * @return  結果
     */
    public int insert(BssAskHistoryEntity bssAskHistory);

     /**
     *問合せ対応履歴情報 を修正
     *
     * @param bssAskHistory 問合せ対応履歴情報 
     * @return  結果
     */
    public int update(BssAskHistoryEntity bssAskHistory);

     /**
     * 問合せ対応履歴情報 を削除
     *
     * @param id 問合せ対応履歴情報 プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
