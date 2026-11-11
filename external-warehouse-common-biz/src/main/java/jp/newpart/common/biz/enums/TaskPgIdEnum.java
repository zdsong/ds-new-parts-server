package jp.newpart.common.biz.enums;

/**
 * Task プログラムＩＤ
 */
public enum TaskPgIdEnum {

    T001("T001", "商品カテゴリ"),
    T002("T002", "商品"),
    T003("T003", "顧客"),
    T004("T004", "注文"),
    T005("T005", "CSV連携");

    private final String code;
    private final String info;

    TaskPgIdEnum(String code, String info)
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
