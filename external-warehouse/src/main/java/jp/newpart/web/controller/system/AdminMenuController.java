package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Anonymous;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysMenu;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysMenuMapper;
import jp.newpart.externalwarehouse.biz.sys.service.ISysMenuService;
import jp.newpart.externalwarehouse.biz.sys.service.ISysUserService;
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

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

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

    /**
     * ログイン中ユーザーが参照可能なメニュー一覧を取得する
     *
     * @return メニュー一覧
     */
    @GetMapping("/myMenus")
    public AjaxResult myMenus()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = userService.selectUserByUserName(loginUser.getUsername());

        // システムユーザーに存在しないログインユーザーは全メニューを表示する
        if (StringUtils.isNull(user))
        {
            return AjaxResult.success(menuMapper.selectMenuTreeAll());
        }

        // 表示対象（表示状態が「表示」かつ有効なメニュー）のみを取得する
        SysMenu menu = new SysMenu();
        menu.setVisible("0");
        menu.setStatus("0");
        return AjaxResult.success(menuService.selectMenuList(menu, user.getUserId()));
    }
}
