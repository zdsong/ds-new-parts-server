package jp.newpart.common.biz.enums;

/**
 * ユーザー認証情報 - 認証方法
 * 0:SMS 1:メール
 */
public enum BssUserCertificationFlag {

    SMS("0", "SMS"),

    MAIL("1", "メール");

    private final String code;
    private final String info;

    BssUserCertificationFlag(String code, String info)
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
