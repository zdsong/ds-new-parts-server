package jp.newpart.common.biz.enums;

/**
 * 農家 プログラムＩＤ
 */
public enum ExternalWarehousePgIdEnum {

    N001("N001","ログイン"),
    N002("N002","パスワードを忘れた方"),
    N003("N003","認証コード入力"),
    N004("N004","パスワード登録"),
    N005("N005","ダッシュボード"),
    N006("N006","注文一覧"),
    N007("N007","注文詳細"),
    N008("N008","商品マスタ一覧"),
    N010("N010","商品マスタ詳細・編集"),
    N011("N011","商品カテゴリマスタ一覧"),
    N013("N013","商品カテゴリマスタ詳細（大）"),
    N014("N014","商品カテゴリマスタ詳細・編集（中）"),
    N015("N015","顧客マスタ一覧"),
    N016("N016","顧客マスタ詳細"),
    N017("N017","お知らせモーダル"),
    N018("N018","メニュー"),
    N019("N019","メッセージポップアップ/モーダル"),
    N020("N020","問い合わせ履歴モーダル"),
    N021("N021","注文検索モーダル"),
    N022("N022","商品検索モーダル"),
    N023("N023","顧客検索モーダル"),
    N024("N024","商品カテゴリ検索モーダル"),
    N025("N025","メッセージポップアップ/モーダル（注文詳細）");

    private final String code;
    private final String info;

    ExternalWarehousePgIdEnum(String code, String info)
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
