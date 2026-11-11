package jp.newpart.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import jp.newpart.common.constant.HttpStatus;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.core.page.PageDomain;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.core.page.TableSupport;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.PageUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * web層共通データ処理
 * 
 * @author nextosd
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * フロントから渡された日付フォーマットの文字列を、自動的にDateタイプに変換する
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * リクエストページングデータの設定
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * リクエストソートデータの設定
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * ページングされたスレッド変数のクリーンアップ
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 応答リクエストページングデータ
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMessage("Success");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 成功を返す
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 失敗メッセージを返す
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 成功メッセージを返す
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 成功メッセージを返す
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 失敗メッセージを返す
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 警告メッセージを返す
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * レスポンスリターン結果
     * 
     * @param rows 影響行数
     * @return 操作結果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * レスポンスリターン結果
     * 
     * @param result 結果
     * @return 結果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * ページジャンプ
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * ユーザーキャッシュ情報の取得
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * ログインユーザIDの取得
     */
    public String getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * ログインユーザー名の取得
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }
}
