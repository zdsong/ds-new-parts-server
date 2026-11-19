package jp.nasta.pms.common.biz.enums;

/**
 * 最後在庫状況（辞書：bss_inventory_last_status）
 */
public enum BssInventoryLastStatusEnum {

    /**
     * 在庫
     */
    PA("PA", "在庫"),

    /**
     * 移動出庫依頼
     */
    TF_OUT("TF_OUT:", "移動出庫依頼"),

    /**
     * 移動出庫完了
     */
    TF_OUTED("TF_OUTED", "移動出庫完了"),

    /**
     * 移動入庫依頼
     */
    TF_IN("TF_IN", "移動入庫依頼"),

    /**
     * 移動入庫完了
     */
    TF_INED("TF_INED", "移動入庫完了"),

    /**
     * 仕入先入庫完了
     */
    PO_INED("PO_INED", "仕入先入庫完了");

    /**
     * 辞書タイプ
     */
    public static final String DICT_TYPE = "bss_inventory_last_status";

    private final String code;
    private final String info;

    BssInventoryLastStatusEnum(String code, String info) {
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
