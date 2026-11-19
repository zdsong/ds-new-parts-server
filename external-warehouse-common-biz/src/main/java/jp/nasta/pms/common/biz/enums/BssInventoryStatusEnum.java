package jp.nasta.pms.common.biz.enums;

/**
 * 在庫状況（辞書：bss_inventory_status）
 */
public enum BssInventoryStatusEnum {

    /**
     * 在庫
     */
    PA("PA", "在庫"),

    /**
     * 移動依頼
     */
    TF("TF", "移動依頼");

    /**
     * 辞書タイプ
     */
    public static final String DICT_TYPE = "bss_inventory_status";

    private final String code;
    private final String info;

    BssInventoryStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
