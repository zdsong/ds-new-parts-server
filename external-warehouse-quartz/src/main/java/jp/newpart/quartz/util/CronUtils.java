package jp.newpart.quartz.util;

import java.text.ParseException;
import java.util.Date;
import org.quartz.CronExpression;

/**
 * cron ツール
 * 
 * @author nextosd
 *
 */
public class CronUtils
{
    /**
     * Cron式の有効性を確認する
     *
     * @param cronExpression Cron式
     * @return boolean
     */
    public static boolean isValid(String cronExpression)
    {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Cron式の有効性を確認する
     *
     * @param cronExpression Cron式
     * @return String 無効な場合は式のエラーの説明を返し、nullを有効に戻す場合は
     */
    public static String getInvalidMessage(String cronExpression)
    {
        try
        {
            new CronExpression(cronExpression);
            return null;
        }
        catch (ParseException pe)
        {
            return pe.getMessage();
        }
    }

    /**
     * 次の実行時間を返します。
     *
     * @param cronExpression Cron式
     * @return Date 次の実行時間
     */
    public static Date getNextExecution(String cronExpression)
    {
        try
        {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
