package jp.newpart.common.core.service;

import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.cache.AppCache;
import jp.newpart.common.exception.user.UserPasswordNotMatchException;
import jp.newpart.common.exception.user.UserPasswordRetryLimitExceedException;
import jp.newpart.common.security.context.AuthenticationContextHolder;
import jp.newpart.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ログインパスワードメソッド
 * 
 * @author nextosd
 */
@Component
public class SysPasswordService
{
    @Autowired
    private AppCache appCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * ログインアカウントパスワードエラー回数キャッシュキー名
     * 
     * @param username ユーザー名
     * @return キャッシュキーkey
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(String userPassword)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = appCache.getCacheObject(getCacheKey(username), Integer.class);

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(userPassword, password))
        {
            retryCount = retryCount + 1;
            appCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(String password, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, password);
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (appCache.hasKey(getCacheKey(loginName)))
        {
            appCache.deleteObject(getCacheKey(loginName));
        }
    }
}
