package jp.newpart.common.biz.enums;

/**
 * お知ら種類
 */
public enum BssNoticeType {

    ORDER("10", "注文情報確認"),

    ITEM("20", "商品マスタ確認 "),

    ITEM_CATEGORY("30", "商品カテゴリマスタ確認"),

    ORDER_MESSAGE("40", "問合せメッセージ(注文情報指定)");

    private final String code;
    private final String info;

    BssNoticeType(String code, String info)
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

