package jp.newpart.common.constant;

/**
 * リターンステータスコード
 * 
 * @author nextosd
 */
public class HttpStatus
{
    /**
     * 操作に成功しました
     */
    public static final int SUCCESS = 200;

    /**
     * オブジェクトの作成に成功しました
     */
    public static final int CREATED = 201;

    /**
     * 要求は受け入れられました
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作は正常に実行されましたが、データは返されませんでした
     */
    public static final int NO_CONTENT = 204;

    /**
     * リソースが削除されました
     */
    public static final int MOVED_PERM = 301;

    /**
     * リダイレクト
     */
    public static final int SEE_OTHER = 303;

    /**
     * リソースは変更されていません
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * パラメータリストエラー（欠落、形式不一致）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 権限なし
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * アクセス制限、権限の期限切れ
     */
    public static final int FORBIDDEN = 403;

    /**
     * リソース、サービスが見つかりません
     */
    public static final int NOT_FOUND = 404;

    /**
     * 許可されていないhttpメソッド
     */
    public static final int BAD_METHOD = 405;

    /**
     * リソースが競合したり、リソースがロックされたりする
     */
    public static final int CONFLICT = 409;

    /**
     * サポートされていないデータ、メディアタイプ
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * システム内部エラー
     */
    public static final int ERROR = 500;

    /**
     * インタフェースが実装されていません
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * システム警告メッセージ
     */
    public static final int WARN = 601;
}
