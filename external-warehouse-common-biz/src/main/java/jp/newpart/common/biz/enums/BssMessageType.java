package jp.newpart.common.biz.enums;

/**
 * メッセージタイプ
 * 0:メッセージの種類 CRM 1:メッセージの種類 農家 2:システムメッセージタイプ
 */
public enum BssMessageType {

    CRM("0", "CRM"),
    FARMER("1", "農家");

    private final String code;
    private final String info;

    BssMessageType(String code, String info)
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

