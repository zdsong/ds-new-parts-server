package jp.newpart.common.enums;

/**
 * 利用状態のステータス
 * 
 * @author nextosd
 */
public enum UsageStatusEnum
{
    OK("0", "利用中"), DISABLE("1", "廃止");

    private final String code;
    private final String info;

    UsageStatusEnum(String code, String info)
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
