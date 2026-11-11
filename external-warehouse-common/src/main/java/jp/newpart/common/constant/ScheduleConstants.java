package jp.newpart.common.constant;

/**
 * タスクスケジューリング共通定数
 * 
 * @author nextosd
 */
public class ScheduleConstants
{
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 実行目標key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** デフォルト */
    public static final String MISFIRE_DEFAULT = "0";

    /** 今すぐ実行をトリガー */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** 1回の実行をトリガする */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** 即時実行 */
    public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status
    {
        /**
         * 正常
         */
        NORMAL("0"),
        /**
         * 一時停止
         */
        PAUSE("1");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
