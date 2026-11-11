package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.BssOrderStatusHistoryEntity;
import java.util.List;


/**
 * 注文ステータス履歴情報Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssOrderStatusHistoryBaseService {

     /**
     * 注文ステータス履歴情報を検索
     *
     * @param id 注文ステータス履歴情報プライマリ・キー
     * @return 注文ステータス履歴情報
     */
    public BssOrderStatusHistoryEntity selectById(String id);

     /**
     * 注文ステータス履歴情報を検索リスト
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報パラメータ対象
     * @return 注文ステータス履歴情報リスト
     */
    public List<BssOrderStatusHistoryEntity> selectList(BssOrderStatusHistoryEntity bssOrderStatusHistory);

     /**
     * 注文ステータス履歴情報を登録
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報
     * @return  結果
     */
    public int insert(BssOrderStatusHistoryEntity bssOrderStatusHistory);

     /**
     * 注文ステータス履歴情報を修正
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報
     * @return  結果
     */
    public int update(BssOrderStatusHistoryEntity bssOrderStatusHistory);

     /**
     * 注文ステータス履歴情報を削除
     *
     * @param id 注文ステータス履歴情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
