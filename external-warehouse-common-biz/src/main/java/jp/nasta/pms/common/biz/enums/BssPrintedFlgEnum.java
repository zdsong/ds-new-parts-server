package jp.nasta.pms.common.biz.enums;

/**
 * パレット番号の印刷フラグ
 * 0:未印刷 1:印刷済み
 */
public enum BssPrintedFlgEnum {

    /**
     * 未印刷
     */
    UNPRINTED("0", "未印刷"),

    /**
     * 印刷済み
     */
    PRINTED("1", "印刷済み");

    private final String code;
    private final String info;

    BssPrintedFlgEnum(String code, String info) {
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
