package jp.newpart.common.core.page;

import jp.newpart.common.text.Convert;
import jp.newpart.common.utils.ServletUtils;

/**
 * データ処理
 * 
 * @author nextosd
 */
public class TableSupport
{
    /**
     * 現在のレコード開始インデックス
     */
    public static final String PAGE_NUM = "current";

    /**
     * ページあたりのレコード数の表示
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * ソート
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * ソートの方向「desc」または「asc」。
     */
    public static final String IS_ASC = "isAsc";

    /**
     * ページングパラメータの合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 改ページ・オブジェクトのカプセル化
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
