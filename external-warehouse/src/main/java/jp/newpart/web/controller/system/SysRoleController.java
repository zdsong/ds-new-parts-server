package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.externalwarehouse.sys.domain.SysUserRole;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.core.service.TokenService;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.externalwarehouse.sys.security.SysPermissionService;
import jp.newpart.externalwarehouse.sys.service.ISysRoleService;
import jp.newpart.externalwarehouse.sys.service.ISysUserService;
import jp.newpart.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ロール情報
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/role")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * ロール番号に基づいて詳細情報を取得
     */
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable String roleId) {
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * ロールを追加
     */
    @Log(title = "ロール管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated({ValidatorGroup.AddGroup.class}) @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            //　入力されたアカウントは既に登録されています。
            return error(MessageUtils.message("E0001", MessageUtils.message("roleName")));
        }
        return toAjax(roleService.insertRole(role));
    }

    /**
     * ロールマスタの論理削除
     */
    @Log(title = "ロール管理", businessType = BusinessType.DELETE)
    @PostMapping("/delLogicRole")
    public AjaxResult deleteLogic(@RequestBody SysRole role) {
        int deleteResult = roleService.deleteLogicRoleByIdAndVersion(role);
        if (deleteResult > 0) {
            roleService.setRoleInfoToCache(role.getRoleId());
        }
        return toAjax(deleteResult);
    }

    /**
     * ロールを更新して保存
     */
    @Log(title = "ロール管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated({ValidatorGroup.UpdateGroup.class}) @RequestBody SysRole role) {
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            //　入力されたアカウントは既に登録されています。
            return error(MessageUtils.message("E0001", MessageUtils.message("roleName")));
        }

        role.setUpdateBy(getUsername());

        if (roleService.updateRole(role) > 0) {
            // キャッシュされたユーザー権限を更新
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUsername()) && !loginUser.getAdminFlag()) {
                // メールアドレスでログインしたユーザーはシステムユーザーに存在しない場合がある
                SysUser user = userService.selectUserByUserName(loginUser.getUsername());
                if (StringUtils.isNotNull(user)) {
                    loginUser.setPermissions(permissionService.getMenuPermission(user));
                    tokenService.setLoginUser(loginUser);
                }
            }
            roleService.setRoleInfoToCache(role.getRoleId());
            return success();
        }
        return error("ロール'" + role.getRoleName() + "'の更新に失敗しました、管理者にお問い合わせください。");
    }

    /**
     * ロールに割り当て済みのユーザー一覧を取得
     */
    @GetMapping("/authUser/allocatedList")
    public AjaxResult allocatedList(SysUser user) {
        return success(userService.selectAllocatedList(user));
    }

    /**
     * ロール未割り当てのユーザー一覧を取得
     */
    @GetMapping("/authUser/unallocatedList")
    public AjaxResult unallocatedList(SysUser user) {
        return success(userService.selectUnallocatedList(user));
    }

    /**
     * ロールにユーザーを追加
     */
    @Log(title = "ロール管理", businessType = BusinessType.INSERT)
    @PostMapping("/authUser")
    public AjaxResult addAuthUsers(@RequestBody SysUser user) {
        return toAjax(roleService.insertAuthUsers(user.getRoleId(), user.getRoleIds()));
    }

    /**
     * ロールからユーザーを削除
     */
    @Log(title = "ロール管理", businessType = BusinessType.DELETE)
    @PostMapping("/authUser/delete")
    public AjaxResult deleteAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * ロール選択リストを取得
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }
}
