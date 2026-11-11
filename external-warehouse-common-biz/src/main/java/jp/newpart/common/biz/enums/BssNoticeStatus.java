package jp.newpart.common.biz.enums;

/**
 * お知らせ Status
 * 0:未読 1:読
 */
public enum BssNoticeStatus {

    UN_READ("0", "未読"),

    READ("1", "既読");

    private final String code;
    private final String info;

    BssNoticeStatus(String code, String info)
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

