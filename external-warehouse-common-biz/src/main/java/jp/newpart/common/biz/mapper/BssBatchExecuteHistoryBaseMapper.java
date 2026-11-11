package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssBatchExecuteHistoryEntity;
import java.util.List;


/**
 * バッチ実行履歴 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssBatchExecuteHistoryBaseMapper {

     /**
     *バッチ実行履歴を検索
     *
     * @param id バッチ実行履歴プライマリ・キー
     * @return バッチ実行履歴
     */
    public BssBatchExecuteHistoryEntity selectById(String id);

     /**
     *バッチ実行履歴を検索リスト
     *
     * @param bssBatchExecuteHistory バッチ実行履歴
     * @return バッチ実行履歴リスト
     */
    public List<BssBatchExecuteHistoryEntity> selectList(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
     *バッチ実行履歴を登録
     *
     * @param bssBatchExecuteHistory バッチ実行履歴
     * @return  結果
     */
    public int insert(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
     *バッチ実行履歴を修正
     *
     * @param bssBatchExecuteHistory バッチ実行履歴
     * @return  結果
     */
    public int update(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
      * バッチ実行履歴を削除
      *
      * @param id バッチ実行履歴プライマリ・キー
      * @return  結果
      */
     public int deleteById(String id);

     /**
      * 連携日に基づいてスケジュール実行履歴を検索
      *
      * @param params 検索パラメータ（cooperationDate, createBy）
      * @return スケジュール実行履歴リスト
      */
     public List<BssBatchExecuteHistoryEntity> getBatchExecuteHistoryByCooperationDate(java.util.Map<String, Object> params);

}
