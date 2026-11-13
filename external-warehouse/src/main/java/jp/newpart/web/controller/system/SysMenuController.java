package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.domain.vo.OptionsKeyValueVo;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysMenu;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.externalwarehouse.sys.service.ISysMenuService;
import jp.newpart.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * メニュー情報
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController extends BaseController {
    /**
     * 管理者ユーザーID
     */
    private static final String ADMIN_USER_ID = "1";

    @Autowired
    private ISysMenuService menuService;

    /**
     * メニューリストを取得
     */
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, ADMIN_USER_ID);
        return success(menus);
    }

    /**
     * メニューIDに基づいて詳細情報を取得
     */
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable String menuId) {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * メニューを追加
     */
    @Log(title = "メニュー管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("新しいメニュー'" + menu.getMenuName() + "'の追加に失敗しました。メニュー名が既に存在します");
        } else if (isFrameYes(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return error("新しいメニュー'" + menu.getMenuName() + "'の追加に失敗しました。アドレスはhttp(s)://で始まらなければなりません");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * メニューを編集
     */
    @Log(title = "メニュー管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("メニュー'" + menu.getMenuName() + "'の編集に失敗しました。メニュー名が既に存在します");
        } else if (isFrameYes(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return error("メニュー'" + menu.getMenuName() + "'の編集に失敗しました。アドレスはhttp(s)://で始まらなければなりません");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return error("メニュー'" + menu.getMenuName() + "'の編集に失敗しました。親メニューには自分自身を選択できません");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * メニューを削除
     */
    @Log(title = "メニュー管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") String menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn("子メニューが存在するため、削除できません");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return warn("メニューが割り当てられているため、削除できません");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     * Parent メニュー コンボボックスデータを取得する
     *
     * @return
     */
    @Anonymous
    @GetMapping(value = "/parent/options")
    public AjaxResult getCommonOptions() {
        SysMenu menuQuery = new SysMenu();
        menuQuery.setParentId("0");
        List<SysMenu> menus = menuService.selectMenuList(menuQuery, ADMIN_USER_ID);

        List<OptionsKeyValueVo> result = new ArrayList<>();

        if(ValueUtils.isNotEmpty(menus)){
            result = menus.stream()
                    .map(item -> OptionsKeyValueVo.builder()
                            .key(item.getMenuId())
                            .value(item.getMenuName()
                            ).build())
                    .collect(Collectors.toList());
        }

        return AjaxResult.success(result);
    }

    /**
     * メニュートリーリストを取得
     */
    @Anonymous
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, ADMIN_USER_ID);
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * is_frame が「はい」かどうかを判定する。
     * 既存データは '0'（旧: はい）、汎用コード sys_yes_no は 'Y' のため、両方を「はい」として扱う。
     *
     * @param isFrame is_frame の値
     * @return 「はい」の場合 true
     */
    private boolean isFrameYes(String isFrame) {
        return UserConstants.YES_FRAME.equals(isFrame) || "Y".equals(isFrame);
    }

    /**
     * 対応するロールのメニュートリーリストを読み込む
     */
    @Anonymous
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable("roleId") String roleId) {
        List<SysMenu> menus = menuService.selectMenuList(ADMIN_USER_ID);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }
}
