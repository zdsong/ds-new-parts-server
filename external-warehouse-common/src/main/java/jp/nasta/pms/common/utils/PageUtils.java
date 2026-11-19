package jp.nasta.pms.common.utils;

import com.github.pagehelper.PageHelper;
import jp.nasta.pms.common.core.page.PageDomain;
import jp.nasta.pms.common.core.page.TableSupport;
import jp.nasta.pms.common.utils.sql.SqlUtil;

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
