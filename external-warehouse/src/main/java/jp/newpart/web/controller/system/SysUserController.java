package jp.newpart.web.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.poi.ExcelUtil;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.externalwarehouse.biz.sys.domain.bo.SysUserQueryBo;
import jp.newpart.externalwarehouse.biz.sys.service.ISysRoleService;
import jp.newpart.externalwarehouse.biz.sys.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import jp.newpart.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ユーザー情報
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/user")
@Tag(name = "ユーザー管理", description = "ユーザー管理API")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * ユーザーリストの取得
     * @param user ユーザー検索
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SysUserQueryBo user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "ユーザー管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserQueryBo user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "ユーザデータ");
    }

    @Log(title = "ユーザー管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "ユーザデータ");
    }

    /**
     * ユーザー番号による詳細の取得
     */
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) String userId)
    {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        //List<SysRole> roles = roleService.selectRolesByUserId(userId);
        //ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(userId))
        {
            SysUser sysUser = userService.selectUserById(userId);
            if(ValueUtils.isNotEmpty(sysUser.getRoles())) {
                sysUser.setRoleId(sysUser.getRoles().get(0).getRoleId());
            }
            sysUser.setPassword(null);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            //ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新規ユーザー
     */
    @Operation(summary = "新規ユーザー", description = "新規ユーザー")
    @Log(title = "ユーザー管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) SysUser user)
    {
        roleService.checkRoleDataScope(user.getRoleId());
        if (!userService.checkUserNameUnique(user))
        {
            return error("新規ユーザー'" + user.getUserName() + "'失敗しました。ログインアカウントは既に存在します");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("新規ユーザー'" + user.getUserName() + "'失敗しました。携帯電話番号はすでに存在します");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("新規ユーザー'" + user.getUserName() + "'失敗しました。メールアカウントは既に存在します");
        }
        user.setCreateBy(getUsername());
        return toAjax(userService.insertUser(user));
    }

    /**
     * ユーザーの変更
     */
    @Log(title = "ユーザー管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        //userService.checkUserDataScope(user.getUserId());
        if(ValueUtils.isNotEmpty(user.getRoleIds())){
            roleService.checkRoleDataScope(user.getRoleIds());
        } else {
            roleService.checkRoleDataScope(user.getRoleId());
        }

        if (!userService.checkUserNameUnique(user))
        {
            return error("ユーザーの変更'" + user.getUserName() + "'失敗しました。ログインアカウントは既に存在します");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("ユーザーの変更'" + user.getUserName() + "'失敗しました。携帯電話番号はすでに存在します");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("ユーザーの変更'" + user.getUserName() + "'失敗しました。メールアカウントは既に存在します");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * ユーザーの削除
     */
    @Log(title = "ユーザー管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("現在のユーザーは削除できません");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * パスワードをリセット
     */
    @Log(title = "ユーザー管理", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody @Validated({ValidatorGroup.CustomerGroup.class}) SysUser user)
    {
        if(ValueUtils.isEmpty(user.getPassword())){
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.password")));
        }
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * ステータスの変更
     */
    @Log(title = "ユーザー管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * ユーザー番号に基づいた権限ロールの取得
     */
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") String userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", user.isAdmin()? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * ユーザー権限ロール
     */
    @Log(title = "ユーザー管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole")
    public AjaxResult insertAuthRole(String userId, String[] roleIds) {
        userService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }
}
