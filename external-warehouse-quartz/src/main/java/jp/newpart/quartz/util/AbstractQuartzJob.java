package jp.newpart.quartz.util;

import jp.newpart.common.biz.utils.ExternalWarehouseUtils;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.ScheduleConstants;
import jp.newpart.common.utils.ExceptionUtil;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.bean.BeanUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.quartz.domain.SysJob;
import jp.newpart.quartz.domain.SysJobLog;
import jp.newpart.quartz.service.ISysJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz呼び出し
 *
 * @author nextosd
 */
public abstract class AbstractQuartzJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * スレッドローカル変数
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try
        {
            before(context, sysJob);
            if (sysJob != null)
            {
                doExecute(context, sysJob);
            }
            after(context, sysJob, null);
        }
        catch (Exception e)
        {
            log.error("タスク実行例外  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 実行前
     *
     * @param context
     * @param sysJob
     */
    protected void before(JobExecutionContext context, SysJob sysJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * 実行後
     *
     * @param context
     * @param sysJob
     */
    protected void after(JobExecutionContext context, SysJob sysJob, Exception e)
    {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobLogId(IdUtils.nextIdStr());
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setEndTime(new Date());
        long runMs = sysJobLog.getEndTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 合計時間：" + runMs + " ミリ秒");
        if (e != null)
        {
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }
        else
        {
            sysJobLog.setStatus(Constants.SUCCESS);
        }

        // データベースに書き込む
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * サブクラスによって再ロードされたメソッドの実行
     *
     * @param context
     * @param sysJob
     * @throws Exception
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
