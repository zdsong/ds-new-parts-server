package jp.newpart.quartz.domain;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.constant.ScheduleConstants;
import jp.newpart.common.core.domain.BaseEntity;
import jp.newpart.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import jp.newpart.quartz.util.CronUtils;

/**
 * スケジュールタスクスケジュール sys_job
 * 
 * @author nextosd
 */
public class SysJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** タスク番号 */
    @Excel(name = "タスク番号", cellType = Excel.ColumnType.STRING)
    private String jobId;

    /** タスク名 */
    @Excel(name = "タスク名")
    private String jobName;

    /** タスクグループ名 */
    @Excel(name = "タスクグループ名")
    private String jobGroup;

    /** 呼び出し先文字列 */
    @Excel(name = "呼び出し先文字列")
    private String invokeTarget;

    /** cronExpression */
    @Excel(name = "cronExpression ")
    private String cronExpression;

    /** 計画ポリシー */
    @Excel(name = "計画ポリシー ", readConverterExp = "0=デフォルト、1=即時実行がトリガーされ、2=1回実行がトリガーされ、3=即時実行がトリガーされない")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** 同時実行するかどうか（0許可1禁止） */
    @Excel(name = "同時実行", readConverterExp = "0=許可,1=禁止")
    private String concurrent;

    /** タスクステータス（0=正常、1=一時停止） */
    @Excel(name = "タスクステータス", readConverterExp = "0=正常、1=一時停止")
    private String status;

    private String remark;

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "タスク名を空白にすることはできません")
    @Size(min = 0, max = 64, message = "タスク名は64文字以内")
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

    @NotBlank(message = "ターゲット文字列を空にすることはできません")
    @Size(min = 0, max = 500, message = "ターゲット文字列の長さは500文字以内")
    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron実行式を空にすることはできません")
    @Size(min = 0, max = 255, message = "Cron実行式は255文字以内")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime()
    {
        if (StringUtils.isNotEmpty(cronExpression))
        {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public String getMisfirePolicy()
    {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy)
    {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent()
    {
        return concurrent;
    }

    public void setConcurrent(String concurrent)
    {
        this.concurrent = concurrent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("cronExpression", getCronExpression())
            .append("nextValidTime", getNextValidTime())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
