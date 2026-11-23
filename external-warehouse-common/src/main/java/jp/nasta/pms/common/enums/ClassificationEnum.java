package jp.nasta.pms.common.enums;

/**
 * 品目区分
 * 辞書 bss_classification に対応する
 *
 * @author nextosd
 */
public enum ClassificationEnum
{
    PRODUCT("1", "製品"), PART("3", "部品");

    private final String code;
    private final String info;

    ClassificationEnum(String code, String info)
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
