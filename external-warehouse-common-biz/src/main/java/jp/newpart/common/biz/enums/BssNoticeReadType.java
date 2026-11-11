package jp.newpart.common.biz.enums;

/**
 * お知らせ 受信者種類
 * 0:CRM 1:農家
 */
public enum BssNoticeReadType {

    CRM("0", "CRM"),

    FARMER("1", "農家");

    private final String code;
    private final String info;

    BssNoticeReadType(String code, String info)
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

