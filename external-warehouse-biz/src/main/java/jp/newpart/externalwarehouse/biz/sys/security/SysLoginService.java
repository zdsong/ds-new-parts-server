package jp.newpart.externalwarehouse.biz.sys.security;

import jakarta.annotation.Resource;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.exception.user.BlackListException;
import jp.newpart.common.exception.user.UserNotExistsException;
import jp.newpart.common.exception.user.UserPasswordNotMatchException;
import jp.newpart.externalwarehouse.biz.sys.service.ISysUserService;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.cache.AppCache;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.manager.AsyncManager;
import jp.newpart.common.manager.factory.AsyncFactory;
import jp.newpart.common.security.context.AuthenticationContextHolder;
import jp.newpart.common.core.service.TokenService;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * ログイン検証方法
 * 
 * @author nextosd
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppCache appCache;
    
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private AppConfig appConfig;

    /**
     * ログイン検証
     * 
     * @param username ユーザー名
     * @param password パスワード
     * @return
     */
    public String login(String username, String password)
    {
        // ログイン前チェック
        loginPreCheck(username, password);
        // ユーザー認証
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, MessageUtils.message("E0004")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_SUCCESS, MessageUtils.message("I0008")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());

        // token
        return tokenService.createToken(loginUser);
    }

    /**
     * ログイン前チェック
     * @param username ユーザー名
     * @param password ユーザーパスワード
     */
    public void loginPreCheck(String username, String password)
    {
        // ユーザー名またはパスワードがNULLのエラー
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, MessageUtils.message("E0052")));
            throw new UserNotExistsException();
        }
        // パスワードが指定された範囲内でなければエラー
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, MessageUtils.message("E0004")));
            throw new UserPasswordNotMatchException();
        }
        // ユーザー名が指定された範囲外のエラー
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, MessageUtils.message("E0004")));
            throw new UserPasswordNotMatchException();
        }
        // IPブラックリスト検証
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.LOGIN_FAIL, MessageUtils.message("E0061")));
            throw new BlackListException();
        }
    }

    /**
     * ロギングログイン情報
     *
     * @param userId ユーザID
     */
    public void recordLoginInfo(String userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
