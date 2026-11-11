package jp.newpart.common.core.domain.server;

import jp.newpart.common.utils.Arith;

/**
 * メモリ関連情報
 * 
 * @author nextosd
 */
public class Mem
{
    /**
     * 総メモリ量
     */
    private double total;

    /**
     * 使用済みメモリ
     */
    private double used;

    /**
     * 残りのメモリ
     */
    private double free;

    public double getTotal()
    {
        return Arith.div(total, (1024 * 1024 * 1024), 2);
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public double getUsed()
    {
        return Arith.div(used, (1024 * 1024 * 1024), 2);
    }

    public void setUsed(long used)
    {
        this.used = used;
    }

    public double getFree()
    {
        return Arith.div(free, (1024 * 1024 * 1024), 2);
    }

    public void setFree(long free)
    {
        this.free = free;
    }

    public double getUsage()
    {
        return Arith.mul(Arith.div(used, total, 4), 100);
    }
}
