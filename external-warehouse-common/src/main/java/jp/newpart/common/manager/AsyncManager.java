package jp.newpart.common.manager;

import jp.newpart.common.utils.Threads;
import jp.newpart.common.utils.spring.SpringUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 非同期タスクマネージャ
 * 
 * @author nextosd
 */
public class AsyncManager
{
    /**
     * 動作遅延10ミリ秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 非同期オペレーションタスクスケジューリングスレッドプール
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * シングルケースモード
     */
    private AsyncManager(){}

    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * タスクの実行
     * 
     * @param task タスク
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * タスクスレッドプールの停止
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
