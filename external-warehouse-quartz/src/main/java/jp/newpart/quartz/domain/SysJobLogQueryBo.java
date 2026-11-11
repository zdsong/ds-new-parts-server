package jp.newpart.quartz.domain;

import lombok.Data;

/**
 * スケジュールタスクスケジュールログテーブル sys_job_log
 *
 * @author nextosd
 */
@Data
public class SysJobLogQueryBo {

    /** ID */
    private Long jobLogId;

    /** タスク名 */
    private String jobName;

    /** タスクグループ名 */
    private String jobGroup;

    private String status;

    /** 開始時間 */
    private String startTime;

    /** 停止時間 */
    private String endTime;
}
