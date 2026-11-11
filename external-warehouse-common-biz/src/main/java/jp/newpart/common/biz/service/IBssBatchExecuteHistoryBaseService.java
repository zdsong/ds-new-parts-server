package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.BssBatchExecuteHistoryEntity;

import java.util.List;


/**
 * スケジュール実行履歴Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssBatchExecuteHistoryBaseService {

     /**
     * スケジュール実行履歴を検索
     *
     * @param id スケジュール実行履歴プライマリ・キー
     * @return スケジュール実行履歴
     */
    public BssBatchExecuteHistoryEntity selectById(String id);

     /**
     * スケジュール実行履歴を検索リスト
     *
     * @param bssBatchExecuteHistory スケジュール実行履歴パラメータ対象
     * @return スケジュール実行履歴リスト
     */
    public List<BssBatchExecuteHistoryEntity> selectList(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
     * スケジュール実行履歴を登録
     *
     * @param bssBatchExecuteHistory スケジュール実行履歴
     * @return  結果
     */
    public int insert(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
     * スケジュール実行履歴を修正
     *
     * @param bssBatchExecuteHistory スケジュール実行履歴
     * @return  結果
     */
    public int update(BssBatchExecuteHistoryEntity bssBatchExecuteHistory);

     /**
      * スケジュール実行履歴を削除
      *
      * @param id スケジュール実行履歴プライマリ・キー
      * @return  結果
      */
     public int deleteById(String id);

     /**
      * 連携日に基づいてスケジュール実行履歴を検索
      *
      * @param cooperationDate 連携日（YYYYMMDD）
      * @param createBy 作成者
      * @return スケジュール実行履歴リスト
      */
     public List<BssBatchExecuteHistoryEntity> getBatchExecuteHistoryByCooperationDate(String cooperationDate, String createBy);

}
