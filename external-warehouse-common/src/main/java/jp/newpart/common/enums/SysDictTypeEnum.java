package jp.newpart.common.enums;

public enum SysDictTypeEnum {

    SYS_YES_NO("sys_yes_no", "システムYes/No"),

    /**
     * 配送方法
     */
    BSS_DLVDELIVERY_FLG("bss_dlvdelivery_flg", "配送方法"),

    /**
     * 荷扱いタイプ
     */
    MAST_HANDLING_TYPE("mast_handling_type", "荷扱いタイプ"),

    /**
     * 宅急便サイズ
     */
    MAST_COURIER_SIZE_TYPE("mast_courier_size_type", "宅急便サイズ"),

    /**
     * 便種タイプ
     */
    MAST_SHIPPING_TYPE("mast_shipping_type", "便種タイプ"),

    /**
     * 除外しない/除外フラグ
     */
    SYS_EXCLUSION_FLG("sys_exclusion_flg", "除外しない/除外フラグ"),

    /**
     * 梱包まとめタイプ
     */
    MAST_ACKAGING_TYPE("mast_ackaging_type", "梱包まとめタイプ"),

    /**
     * 定期購入専用フラグ
     */
    MAST_SUB_ONLY_FLG("mast_sub_only_flg", "定期購入専用フラグ"),

    /**
     * 無効/有効フラグ
     */
    SYS_INVALID_VALID_FLG("sys_invalid_valid_flg", "無効/有効フラグ"),

    /**
     * 非表示/表示フラグ
     */
    SYS_NDICATOR_FLG("sys_ndicator_flg", "非表示/表示フラグ"),

    /**
     * 注文不可指定
     */
    MAST_ORDER_PROHIBIT_SPEC_FLG("mast_order_prohibit_spec_flg", "注文不可指定");

    private final String code;
    private final String info;

    SysDictTypeEnum(String code, String info)
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
