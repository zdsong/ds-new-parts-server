package jp.nasta.pms.common.biz.enums;

/**
 * 在庫履歴ステータス（辞書：bss_inventory_hist_status）
 */
public enum BssInventoryHistStatusEnum {

    /**
     * 移動予定
     */
    TF_P("TF_P", "移動予定"),

    /**
     * 移動完了
     */
    TF("TF", "移動完了"),

    /**
     * 入庫済
     */
    IN("IN", "入庫済");

    /**
     * 辞書タイプ
     */
    public static final String DICT_TYPE = "bss_inventory_hist_status";

    private final String code;
    private final String info;

    BssInventoryHistStatusEnum(String code, String info) {
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
