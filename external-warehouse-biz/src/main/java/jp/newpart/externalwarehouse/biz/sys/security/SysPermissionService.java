package jp.newpart.externalwarehouse.biz.sys.security;

import jp.newpart.externalwarehouse.biz.sys.service.ISysMenuService;
import jp.newpart.externalwarehouse.biz.sys.service.ISysRoleService;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ユーザ権限処理
 * 
 * @author nextosd
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * ロールデータ権限の取得
     * 
     * @param user ユーザー情報
     * @return ロール権限情報
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理者はすべての権限を持っています
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * メニューデータ権限の取得
     * 
     * @param user ユーザー情報
     * @return メニュー権限情報
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理者はすべての権限を持っています
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles))
            {
                // マルチロールでpermissionsプロパティを設定して、データ権限が権限に一致するようにします
                for (SysRole role : roles)
                {
                    if (StringUtils.equals(role.getStatus(), UserConstants.ROLE_NORMAL))
                    {
                        Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                        role.setPermissions(rolePerms);
                        perms.addAll(rolePerms);
                    }
                }
            }
            else
            {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
