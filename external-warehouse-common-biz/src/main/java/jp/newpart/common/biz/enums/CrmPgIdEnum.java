package jp.newpart.common.biz.enums;

/**
 * CRM プログラムＩＤ
 */
public enum CrmPgIdEnum {

    C001("C001", "ログイン"),
    C002("C002", "お問い合わせ登録"),
    C003("C003", "お問い合わせ一覧"),
    C004("C004", "お問い合わせ更新"),
    C005("C005", "農家メッセージ一覧"),
    C006("C006", "農家メッセージ詳細"),
    C007("C007", "顧客管理"),
    C008("C008", "顧客情報詳細"),
    C009("C009", "顧客情報登録・編集"),
    C010("C010", "農家管理"),
    C011("C011", "農家情報詳細"),
    C012("C012", "農家情報登録・編集"),
    C013("C013", "ユーザ管理"),
    C014("C014", "ユーザ情報詳細"),
    C015("C015", "ユーザ情報登録・編集"),
    C016("C016", "注文一覧"),
    C022("C022", "注文検索モーダル"),
    C023("C023", "お問い合わせ検索モーダル"),
    C024("C024", "顧客検索モーダル"),
    C025("C025", "対応履歴モーダル"),
    C026("C026", "農家メッセージ種別選択モーダル");

    private final String code;
    private final String info;

    CrmPgIdEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
