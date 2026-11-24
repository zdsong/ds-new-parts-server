package jp.nasta.pms.common.biz.enums;

/**
 * 移動状況（辞書：bss_transfer_status）
 */
public enum BssTransferStatusEnum {

    /**
     * 移動依頼
     */
    TF("TF", "移動依頼"),
    /**
     * 移動依頼
     */
    TF_P("TF_P", "移動予定"),
    /**
     * 移動依頼
     */
    TF_F("TF_F", "移動完了");;

    /**
     * 辞書タイプ
     */
    public static final String DICT_TYPE = "bss_transfer_status";

    private final String code;
    private final String info;

    BssTransferStatusEnum(String code, String info) {
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
