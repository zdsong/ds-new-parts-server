package jp.newpart.quartz.domain;

import jp.newpart.common.annotation.Excel;
import jp.newpart.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * スケジュールタスクスケジュールログテーブル sys_job_log
 * 
 * @author nextosd
 */
public class SysJobLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ログ番号")
    private String jobLogId;

    /** タスク名 */
    @Excel(name = "タスク名")
    private String jobName;

    /** タスクグループ名 */
    @Excel(name = "タスクグループ名")
    private String jobGroup;

    /** ターゲット文字列 */
    @Excel(name = "ターゲット文字列")
    private String invokeTarget;

    /** ログ情報 */
    @Excel(name = "ログ情報")
    private String jobMessage;

    /** 実行ステータス（0=正常、1=失敗） */
    @Excel(name = "実行ステータス", readConverterExp = "0=正常、1=失敗")
    private String status;

    /** 例外情報 */
    @Excel(name = "例外情報")
    private String exceptionInfo;

    /** 開始時間 */
    private Date startTime;

    /** 停止時間 */
    private Date endTime;

    public String getJobLogId()
    {
        return jobLogId;
    }

    public void setJobLogId(String jobLogId)
    {
        this.jobLogId = jobLogId;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    public String getJobMessage()
    {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage)
    {
        this.jobMessage = jobMessage;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getExceptionInfo()
    {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo)
    {
        this.exceptionInfo = exceptionInfo;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobLogId", getJobLogId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("jobMessage", getJobMessage())
            .append("status", getStatus())
            .append("exceptionInfo", getExceptionInfo())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .toString();
    }
}
