package jp.newpart.common.constant;

import io.jsonwebtoken.Claims;

import java.util.Locale;

/**
 * 汎用定数情報
 * 
 * @author nextosd
 */
public class Constants
{
    /**
     * UTF-8 文字セット
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 文字セット
     */
    public static final String GBK = "GBK";

    /**
     * Shift_JIS 文字セット
     */
    public static final String SHIFT_JIS = "Shift_JIS";

    /**
     * システム言語
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * www domain
     */
    public static final String WWW = "www.";

    /**
     * http
     */
    public static final String HTTP = "http://";

    /**
     * https
     */
    public static final String HTTPS = "https://";

    /**
     * Success
     */
    public static final String SUCCESS = "0";

    /**
     * 失敗
     */
    public static final String FAIL = "1";

    /**
     * ログイン成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * Logout
     */
    public static final String LOGOUT = "Logout";

    /**
     * Register
     */
    public static final String REGISTER = "Register";

    /**
     * Error
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * すべての権限ID
     */
    public static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理者ロール権限ID
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * ユーザーの種類: 管理者
     */
    public static final String USER_TYPE_SUPER_ADMIN = "90";

    /**
     * ロール権限区切り文字
     */
    public static final String ROLE_DELIMETER = ",";

    /**
     * 権限IDセパレータ
     */
    public static final String PERMISSION_DELIMETER = ",";

    /**
     * 検証コード有効期間（分）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * トークン
     */
    public static final String TOKEN = "accessToken";

    /**
     * トークン接頭辞
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Login User Key
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * Login User Key
     */
    public static final String LOGIN_TEMP_USER_KEY = "login_temp_user_key";

    /**
     * User Id
     */
    public static final String JWT_USERID = "userid";

    /**
     * User Name
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * User avatr
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * Create Time
     */
    public static final String JWT_CREATED = "created";

    /**
     * ユーザー権限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * リソースマッピングパス接頭辞
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * jsonオブジェクトのホワイトリスト構成を自動的に識別する（解析可能なパケット名のみ、範囲が小さいほど安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "jp.newpart" };

    /**
     * スケジュールタスクのホワイトリスト構成（他の必要な場合は自分で追加できるアクセス可能なパッケージ名のみ）
     */
    public static final String[] JOB_WHITELIST_STR = { "jp.newpart.quartz.task" };

    /**
     * タイミングタスク違反の文字
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "jp.newpart.common.utils.file", "jp.newpart.common.config", "jp.newpart.generator" };

    /**
     * カンマ
     */
    public static final char COMMA = ',';
}
