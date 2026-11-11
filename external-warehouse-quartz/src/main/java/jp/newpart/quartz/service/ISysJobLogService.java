package jp.newpart.quartz.service;

import jp.newpart.quartz.domain.SysJobLog;
import jp.newpart.quartz.domain.SysJobLogQueryBo;

import java.util.List;

/**
 * 定時タスクスケジュールログ情報 サービス層
 *
 * @author nextosd
 */
public interface ISysJobLogService {
    /**
     * quartzスケジューラーのログの計画タスクを取得する
     *
     * @param jobLog スケジュールログ情報
     * @return スケジュールタスクログのリスト
     */
    public List<SysJobLog> selectJobLogList(SysJobLogQueryBo jobLog);

    /**
     * スケジュールタスクログIDでスケジュール情報を查詢する
     *
     * @param jobLogId スケジュールタスクログID
     * @return スケジュールタスクログオブジェクト情報
     */
    public SysJobLog selectJobLogById(String jobLogId);

    /**
     * 新規タスクログを追加する
     *
     * @param jobLog スケジュールログ情報
     */
    public void addJobLog(SysJobLog jobLog);

    /**
     * スケジュールログ情報を一括削除する
     *
     * @param logIds 削除が必要なログID
     * @return 結果
     */
    public int deleteJobLogByIds(String[] logIds);

    /**
     * タスクログを削除する
     *
     * @param jobId スケジュールログID
     * @return 結果
     */
    public int deleteJobLogById(String jobId);

    /**
     * タスクログをクリアする
     */
    public void cleanJobLog();
}
