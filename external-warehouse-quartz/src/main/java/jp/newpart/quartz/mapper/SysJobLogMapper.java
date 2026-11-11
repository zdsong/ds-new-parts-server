package jp.newpart.quartz.mapper;

import jp.newpart.quartz.domain.SysJobLog;
import jp.newpart.quartz.domain.SysJobLogQueryBo;

import java.util.List;

/**
 * スケジュールタスクログ情報
 * 
 * @author nextosd
 */
public interface SysJobLogMapper
{
    /**
     * quartzスケジューラのログを取得する計画タスク
     * 
     * @param jobLog スケジュールログ情報
     * @return タスクログコレクションのスケジュール
     */
    public List<SysJobLog> selectJobLogList(SysJobLogQueryBo jobLog);

    /**
     * すべてのスケジュールされたタスクログの問合せ
     *
     * @return スケジュールタスクログリスト
     */
    public List<SysJobLog> selectJobLogAll();

    /**
     * スケジューリングタスクログIDによるスケジューリング情報の照会
     * 
     * @param jobLogId スケジュールタスクログID
     * @return
     */
    public SysJobLog selectJobLogById(String jobLogId);

    /**
     * 新規タスクログ
     * 
     * @param jobLog スケジュールログ情報
     * @return
     */
    public int insertJobLog(SysJobLog jobLog);

    /**
     * スケジュール・ログ情報の一括削除
     * 
     * @param logIds ID List
     * @return
     */
    public int deleteJobLogByIds(String[] logIds);

    /**
     * タスクログの削除
     * 
     * @param jobId ID
     * @return
     */
    public int deleteJobLogById(String jobId);

    /**
     * すべてのタスクログの削除
     */
    public void cleanJobLog();
}
