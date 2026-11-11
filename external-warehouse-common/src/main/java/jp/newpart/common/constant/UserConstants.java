package jp.newpart.common.constant;

/**
 * ユーザ定数情報
 * 
 * @author nextosd
 */
public class UserConstants
{
    /**
     * プラットフォーム内のシステムユーザのユニークなフラグ
     */
    public static final String SYS_USER = "SYS_USER";

    /** 通常の状態 */
    public static final String NORMAL = "0";

    /** 異常 */
    public static final String EXCEPTION = "1";

    /** ユーザ閉鎖状態 */
    public static final String USER_DISABLE = "1";

    /** ロールの通常の状態 */
    public static final String ROLE_NORMAL = "0";

    /** ロール閉鎖状態 */
    public static final String ROLE_DISABLE = "1";

    /** 辞書の通常の状態 */
    public static final String DICT_NORMAL = "0";

    /** システムのデフォルトかどうか（Yes） */
    public static final String YES = "Y";

    /** メニュー外チェーンの有無（Yes） */
    public static final String YES_FRAME = "0";

    /** メニュー外チェーンの有無（No） */
    public static final String NO_FRAME = "1";

    /** メニュータイプ（ディレクトリ） */
    public static final String TYPE_DIR = "M";

    /** メニュータイプ（メニュー） */
    public static final String TYPE_MENU = "C";

    /** メニュータイプ（ボタン） */
    public static final String TYPE_BUTTON = "F";

    /** LayoutコンポーネントID */
    public final static String LAYOUT = "Layout";
    
    /** ParentViewコンポーネントID */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLinkコンポーネントID */
    public final static String INNER_LINK = "InnerLink";

    /** 一意の戻りIDを検証する */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * ユーザー名の長さ制限
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * パスワード長の制限
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
}
