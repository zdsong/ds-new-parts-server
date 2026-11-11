package jp.newpart.common.biz.constants;

/**
 * 農家支援 & CRM システム 共通定数情報
 */
public class ExternalWarehouseConstants {

    /**
     * 新・産直くん 二次認証コード長
     */
    public static final String SANCHOKU_AUTH_CODE_LENGTH = "sanchoku.auth.code.length";

    /**
     * 新・産直くん 二次認証有効期間(分)
     */
    public static final String SANCHOKU_AUTH_CODE_EXPIRE_MINUTES = "sanchoku.auth.code.expireMinutes";

    /**
     * 新・産直くん 登録メールアドレスに認証コードが送信コンテンツ
     */
    public static final String SANCHOKU_AUTH_CODE_LOGIN_EMAIL_CONTENT = "sanchoku.auth.code.login.email.content";

    /**
     * 新・産直くん 登録メールアドレスに認証コードが送信タイトル
     */
    public static final String SANCHOKU_AUTH_CODE_LOGIN_EMAIL_TITLE = "sanchoku.auth.code.login.email.title";

    /**
     * 新・産直くん 登録メールアドレスに認証コードがBccメールアドレス
     */
    public static final String SANCHOKU_AUTH_CODE_BCC_EMAIL_ADDRESS = "sanchoku.auth.code.bcc.email.address";

    /**
     * CRM 登録メールアドレスに認証コードが送信タイトル
     */
    public static final String CRM_AUTH_CODE_LOGIN_EMAIL_TITLE = "crm.auth.code.login.email.title";

    /**
     * CRM 登録メールアドレスに認証コードが送信コンテンツ
     */
    public static final String CRM_AUTH_CODE_LOGIN_EMAIL_CONTENT = "crm.auth.code.login.email.content";

    /**
     * CRM 農家登録が完了すると、メールが送信されます。
     */
    public static final String CRM_FARMER_ADD_SUCCESS_SEND_EMAIL= "crm.farmer.add.success.send.mail";

    /**
     * CSVインポート CSVファイル Charset
     */
    public static final String SYS_CSV_READER_CHARSET = "sys.csv.reader.charset";

    /**
     *  CRM->農家メッセージ送信->注文情報指定->お知らせ注文情報確認->コンテンツ
     */
    public static final String CRM_SEND_ASK_MESSAGE_NOTICE_CONTENT = "crm.send.ask.message.notice.content";

    /**
     *  半角スペース
     */
    public static final String HALF_WIDTH_SPACE =" ";
    /**
     *  左括弧（半角）
     */
    public static final String HALF_BRACKET_LEFT ="(";
    /**
     *  右括弧（半角）
     */
    public static final String HALF_BRACKET_RIGHT =")";

    /**
     * CRM 農家を追加/削除するときにBccメールアドレスを送信コンテンツ
     */
    public static final String CRM_FARMER_ADD_DELETE_BCC_EMAIL_ADDRESS = "crm.farmer.add.discontinued.bcc.mail.address";
    /**
     * 新CRMの農家情報変更通知書のタイトル
     */
    public static final String CRM_FARMER_ADD_DELETE_SEND_EMAIL_TITLE = "crm.farmer.add.discontinued.send.mail.title";

    /**
     * CRM 農家を追加/削除するときにメールアドレスを送信コンテンツ
     */
    public static final String CRM_FARMER_ADD_DELETE_SEND_EMAIL_CONTENT = "crm.farmer.add.discontinued.send.mail.context";

    /**
     * メッセージングを再度有効にする
     */
    public static final String BSS_MESSAGE_BEGIN_LABEL = "によって、メッセージのやり取りが再開しました。";
    /**
     * メッセージングを無効にする
     */
    public static final String BSS_MESSAGE_END_LABEL = "によって、このメッセージは終了しました。";

    /**
     * お客様がログイン時に農家を選択しなかった場合、デフォルトの農家IDが使用されます。
     */
    public static final String MAST_CUSTOMER_SELECT_FARMER_ID = "000000000";
}
