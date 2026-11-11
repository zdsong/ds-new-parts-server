package jp.newpart.common.core.service;

import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.security.context.PermissionContextHolder;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * カスタム権限実装、ssはSpringSecurityの頭文字から取得
 * 
 * @author nextosd
 */
@Service("ss")
public class PermissionService
{
    /**
     * ユーザーが権限を持っていることを確認します
     * 
     * @param permission 権限文字列
     * @return ユーザーに権限があるかどうか
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 権限を含むかどうかを判断する
     * 
     * @param permissions 権限リスト
     * @param permission 権限文字列
     * @return ユーザーに権限があるかどうか
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
