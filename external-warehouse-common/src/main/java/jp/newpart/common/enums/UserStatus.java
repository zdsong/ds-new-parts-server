package jp.newpart.common.enums;

/**
 * 利用状態のステータス
 * 
 * @author nextosd
 */
public enum UserStatus
{
    OK("0", "正常"), DISABLE("1", "停止"), DELETED("2", "削除");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
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
