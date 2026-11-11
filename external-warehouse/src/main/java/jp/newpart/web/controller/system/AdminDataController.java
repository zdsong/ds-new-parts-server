package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Anonymous;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.externalwarehouse.biz.sys.mapper.AdminDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理画面用の参照情報を返すコントローラー
 *
 * @author nextosd
 */
@RestController
public class AdminDataController
{
    @Autowired
    private AdminDataMapper adminDataMapper;

    /**
     * カテゴリ一覧を取得する
     *
     * @return カテゴリ一覧
     */
    @Anonymous
    @GetMapping("/api/system/category/list")
    public AjaxResult categoryList()
    {
        return AjaxResult.success(adminDataMapper.selectCategoryList());
    }

    /**
     * 農家メッセージ一覧を取得する
     *
     * @return メッセージ一覧
     */
    @Anonymous
    @GetMapping("/api/system/message/list")
    public AjaxResult messageList()
    {
        return AjaxResult.success(adminDataMapper.selectMessageList());
    }

    /**
     * システムバージョン情報を取得する
     *
     * @return バージョン情報
     */
    @Anonymous
    @GetMapping("/api/system/version")
    public AjaxResult version()
    {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null)
        {
            version = "1.0.0";
        }
        AjaxResult ajax = AjaxResult.success();
        java.util.Map<String, Object> info = new java.util.HashMap<>();
        info.put("appName", "external-warehouse");
        info.put("version", version);
        info.put("javaVersion", System.getProperty("java.version"));
        ajax.put("data", info);
        return ajax;
    }
}
