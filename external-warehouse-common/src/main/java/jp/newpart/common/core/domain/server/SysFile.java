package jp.newpart.common.core.domain.server;

/**
 * システムファイル関連情報
 * 
 * @author nextosd
 */
public class SysFile
{
    /**
     * ディスクパス
     */
    private String dirName;

    /**
     * ディスクタイプ
     */
    private String sysTypeName;

    /**
     * ファイルの種類
     */
    private String typeName;

    /**
     * 合計サイズ
     */
    private String total;

    /**
     * 残りのサイズ
     */
    private String free;

    /**
     * 使用済み
     */
    private String used;

    /**
     * リソースの使用率
     */
    private double usage;

    public String getDirName()
    {
        return dirName;
    }

    public void setDirName(String dirName)
    {
        this.dirName = dirName;
    }

    public String getSysTypeName()
    {
        return sysTypeName;
    }

    public void setSysTypeName(String sysTypeName)
    {
        this.sysTypeName = sysTypeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public String getFree()
    {
        return free;
    }

    public void setFree(String free)
    {
        this.free = free;
    }

    public String getUsed()
    {
        return used;
    }

    public void setUsed(String used)
    {
        this.used = used;
    }

    public double getUsage()
    {
        return usage;
    }

    public void setUsage(double usage)
    {
        this.usage = usage;
    }
}
