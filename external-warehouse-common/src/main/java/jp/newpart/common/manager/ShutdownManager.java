package jp.newpart.common.manager;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * アプリケーションの終了時にバックグラウンドスレッドを閉じることができることを確認します
 *
 * @author nextosd
 */
@Component
public class ShutdownManager
{
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy()
    {
        shutdownAsyncManager();
    }

    /**
     * 非同期タスクの停止
     */
    private void shutdownAsyncManager()
    {
        try
        {
            logger.info("====バックグラウンドタスクスレッドプールを閉じる====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
