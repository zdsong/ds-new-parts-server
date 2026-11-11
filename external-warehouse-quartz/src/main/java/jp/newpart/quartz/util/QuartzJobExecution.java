package jp.newpart.quartz.util;

import jp.newpart.quartz.domain.SysJob;
import org.quartz.JobExecutionContext;

/**
 * スケジュールタスク処理（同時実行を許可）
 * 
 * @author nextosd
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
