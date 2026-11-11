package jp.newpart.common.biz.enums;

/**
 * ユーザー認証情報 - 種類
 * 0:CRM 1:農家
 */
public enum BssUserCertificationType {

    CRM("0", "CRM"),

    FRAMER("1", "農家");

    private final String code;
    private final String info;

    BssUserCertificationType(String code, String info)
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
