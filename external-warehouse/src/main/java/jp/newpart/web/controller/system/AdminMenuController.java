package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Anonymous;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysMenu;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理メニュー情報の取得
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/menu")
public class AdminMenuController
{
    @Autowired
    private SysMenuMapper menuMapper;

    /**
     * システムメニュー一覧を取得する
     *
     * @return メニュー一覧
     */
    @Anonymous
    @GetMapping("/adminMenus")
    public AjaxResult adminMenus()
    {
        List<SysMenu> menus = menuMapper.selectMenuTreeAll();
        return AjaxResult.success(menus);
    }
}
