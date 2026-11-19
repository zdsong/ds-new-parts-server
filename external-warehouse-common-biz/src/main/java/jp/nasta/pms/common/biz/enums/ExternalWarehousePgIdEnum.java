package jp.nasta.pms.common.biz.enums;

/**
 * 農家 プログラムＩＤ
 */
public enum ExternalWarehousePgIdEnum {

    N001("N001","ログイン"),
    N002("N002","パスワードを忘れた方"),
    N003("N003","認証コード入力"),
    N004("N004","パスワード登録"),
    N005("N005","ダッシュボード"),
    N018("N018","メニュー"),
    P303("P303","パレット番号発番"),
    SP303("SP303","パレット番号発番"),
    SP304("SP304","パレット番号一覧"),
    SP305("SP305","パレット情報詳細"),
    P305("S305","パレット情報詳細"),
    P601("P601","ロケ番一覧"),
    P602("P602","ロケ番新規登録"),
    P603("P603","ロケ番更新");

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
