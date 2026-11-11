package jp.newpart.common.enums;

/**
 * 廃止フラグのステータス
 * 
 * @author nextosd
 */
public enum DiscontinuedFlgEnum
{
    OK("0", "有効"), DISABLE("1", "廃止");

    private final String code;
    private final String info;

    DiscontinuedFlgEnum(String code, String info)
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
