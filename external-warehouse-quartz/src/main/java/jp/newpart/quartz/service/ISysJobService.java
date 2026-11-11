package jp.newpart.quartz.service;

import java.util.List;

import jp.newpart.common.exception.job.TaskException;
import org.quartz.SchedulerException;
import jp.newpart.quartz.domain.SysJob;

/**
 * 定時タスクスケジュール情報 サービス層
 *
 * @author nextosd
 */
public interface ISysJobService {
    /**
     * quartzスケジューラーの計画タスクを取得する
     *
     * @param job スケジュール情報
     * @return スケジュールタスクのリスト
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * スケジュールタスクIDでスケジュール情報を查詢する
     *
     * @param jobId スケジュールタスクID
     * @return スケジュールタスクオブジェクト情報
     */
    public SysJob selectJobById(String jobId);

    /**
     * タスクを一時停止する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを停止する場合にスローされます
     */
    public int pauseJob(SysJob job) throws SchedulerException;

    /**
     * タスクを再開する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを再開する場合にスローされます
     */
    public int resumeJob(SysJob job) throws SchedulerException;

    /**
     * タスクを削除すると、対応するトリガーも削除される
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを削除する場合にスローされます
     */
    public int deleteJob(SysJob job) throws SchedulerException;

    /**
     * スケジュール情報を一括削除する
     *
     * @param jobIds 削除が必要なタスクID
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを削除する場合にスローされます
     */
    public void deleteJobByIds(String[] jobIds) throws SchedulerException;

    /**
     * タスクスケジュール状態を変更する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを変更する場合にスローされます
     */
    public int changeStatus(SysJob job) throws SchedulerException;

    /**
     * タスクを即時実行する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを実行する場合にスローされます
     */
    public boolean run(SysJob job) throws SchedulerException;

    /**
     * 新規タスクを追加する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを追加する場合にスローされます
     * @throws TaskException      タスクのスケジュールを追加する場合にスローされます
     */
    public int insertJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * タスクを更新する
     *
     * @param job スケジュール情報
     * @return 結果
     * @throws SchedulerException タスクのスケジュールを更新する場合にスローされます
     * @throws TaskException      タスクのスケジュールを更新する場合にスローされます
     */
    public int updateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * cron表現式が有効かどうかを検証する
     *
     * @param cronExpression 表現式
     * @return 結果
     */
    public boolean checkCronExpressionIsValid(String cronExpression);
}
