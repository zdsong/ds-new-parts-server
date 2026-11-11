package jp.newpart.quartz.service.impl;

import java.util.List;
import jakarta.annotation.PostConstruct;
import jp.newpart.common.biz.utils.ExternalWarehouseUtils;
import jp.newpart.common.constant.ScheduleConstants;
import jp.newpart.common.exception.job.TaskException;
import jp.newpart.common.utils.uuid.IdUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.newpart.quartz.domain.SysJob;
import jp.newpart.quartz.mapper.SysJobMapper;
import jp.newpart.quartz.service.ISysJobService;
import jp.newpart.quartz.util.CronUtils;
import jp.newpart.quartz.util.ScheduleUtils;

/**
 * 定時タスクスケジューリング情報 サービス層
 *
 * @author nextosd
 */
@Service
public class SysJobServiceImpl implements ISysJobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysJobMapper jobMapper;

    /**
     * プロジェクト起動時に、タイマーアプリケーションを初期化する
     * 主に手動でデータベースを変更して、スケジュールタスク処理と同期していない場合を防ぐ（注：データベースIDとタスクグループ名を手動で変更しないでください、それによりダーティデータが発生します）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * quartzスケジューラーの計画タスクリストを取得する
     *
     * @param job スケジュール情報
     * @return
     */
    @Override
    public List<SysJob> selectJobList(SysJob job) {
        return jobMapper.selectJobList(job);
    }

    /**
     * スケジュールタスクIDでスケジュール情報を取得する
     *
     * @param jobId スケジュールタスクID
     * @return スケジュールタスクオブジェクト情報
     */
    @Override
    public SysJob selectJobById(String jobId) {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * タスクを一時停止する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob job) throws SchedulerException {
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * タスクを再開する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException {
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * タスクを削除する。対応するトリガーも削除される
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException {
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 複数のスケジュール情報を一括削除する
     *
     * @param jobIds 削除が必要なタスクID
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(String[] jobIds) throws SchedulerException {
        for (String jobId : jobIds) {
            SysJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * タスクスケジューリング状態を変更する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * タスクをすぐに実行する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob job) throws SchedulerException {
        boolean result = false;
        String jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        SysJob properties = selectJobById(job.getJobId());
        // パラメータ
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }

    /**
     * 新規タスクを追加する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob job) throws SchedulerException, TaskException {
        job.setJobId(IdUtils.nextIdStr());
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * タスクの時間表現を更新する
     *
     * @param job スケジュール情報
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob job) throws SchedulerException, TaskException {
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * タスクを更新する
     *
     * @param job      タスクオブジェクト
     * @param jobGroup タスクグループ名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        String jobId = job.getJobId();
        // 存在チェック
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 作成時のデータ問題を防ぐために、まず削除し、その後作成操作を実行する
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * cron表現が有効かどうかを検証する
     *
     * @param cronExpression 表現
     * @return 結果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }
}
