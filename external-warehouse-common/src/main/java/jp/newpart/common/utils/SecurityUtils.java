package jp.newpart.common.utils;

import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.HttpStatus;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

/**
 * セキュリティサービスツールクラス
 * 
 * @author nextosd
 */
public class SecurityUtils
{

    /**
     * User ID
     **/
    public static String getUserId()
    {
        try
        {
            return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            throw new ServiceException("取得ユーザID異常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * ユーザーアカウントの取得
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new ServiceException("ユーザーアカウント例外の取得", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * ユーザーの取得
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new ServiceException("ユーザー情報の取得例外", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Authenticationの取得
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * BCryptPasswordEncoderパスワードの生成
     *
     * @param password パスワード
     * @return 暗号化文字列
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * パスワードが同じかどうかを判断する
     *
     * @param rawPassword リアルパスワード
     * @param encodedPassword リアルパスワード
     * @return
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 管理者かどうか
     *
     * @return
     */
    public static boolean isAdmin()
    {
        return getLoginUser().getAdminFlag();
    }

    /**
     * ユーザーが権限を持っていることを確認します
     * 
     * @param permission 権限文字列
     * @return ユーザーに権限があるかどうか
     */
    public static boolean hasPermi(String permission)
    {
        return hasPermi(getLoginUser().getPermissions(), permission);
    }

    /**
     * 権限を含むかどうかを判断する
     * 
     * @param authorities 権限リスト
     * @param permission 権限文字列
     * @return ユーザーに権限があるかどうか
     */
    public static boolean hasPermi(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * ロールを含むかどうかを判断する
     * 
     * @param roles ロールのリスト
     * @param role ロール
     * @return ユーザーがロール権限を持っているかどうか
     */
    public static boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }

}
