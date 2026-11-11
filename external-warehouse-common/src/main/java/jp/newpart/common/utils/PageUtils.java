package jp.newpart.common.utils;

import com.github.pagehelper.PageHelper;
import jp.newpart.common.core.page.PageDomain;
import jp.newpart.common.core.page.TableSupport;
import jp.newpart.common.utils.sql.SqlUtil;

/**
 * ページングツールクラス
 * 
 * @author nextosd
 */
public class PageUtils extends PageHelper
{
    /**
     * リクエストページングデータの設定
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * ページングされたスレッド変数のクリーンアップ
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
