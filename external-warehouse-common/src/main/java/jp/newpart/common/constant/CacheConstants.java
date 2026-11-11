package jp.newpart.common.constant;

/**
 * キャッシュキー定数
 * 
 * @author nextosd
 */
public class CacheConstants {

    public static final String CACHE_TOOL_REDIS = "redis";

    public static final String CACHE_TOOL_DB = "db";

    /**
     * ログインユーザー redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 認証コード redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * パラメータ管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 辞書管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 再送信防止 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * Rate redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * ログインアカウントパスワードエラー回数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * ロールIDで権限のキャッシュキー
     */
    public static final String SYS_ROLE_MENU_PERM_KEY = "sys_role_menu_perm:";
}
