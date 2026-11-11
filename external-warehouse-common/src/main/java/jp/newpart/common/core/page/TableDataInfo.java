package jp.newpart.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表ページングデータオブジェクト
 * 
 * @author nextosd
 */
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 合計レコード数 */
    private long total;

    /** リストデータ */
    private List<?> rows;

    /** メッセージステータスコード */
    private int code;

    /** メッセージの内容 */
    private String message;

    /**
     * 表データオブジェクト
     */
    public TableDataInfo()
    {
    }

    /**
     * ページネーション
     * 
     * @param list リストデータ
     * @param total 合計レコード数
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String msg)
    {
        this.message = message;
    }
}
