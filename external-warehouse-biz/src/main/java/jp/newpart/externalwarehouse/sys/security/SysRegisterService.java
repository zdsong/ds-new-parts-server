package jp.newpart.externalwarehouse.sys.security;

import jp.newpart.common.cache.AppCache;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.sys.service.ISysUserService;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.RegisterBody;
import jp.newpart.common.exception.user.CaptchaException;
import jp.newpart.common.exception.user.CaptchaExpireException;
import jp.newpart.common.manager.AsyncManager;
import jp.newpart.common.manager.factory.AsyncFactory;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登録検証方法
 * 
 * @author nextosd
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private AppConfig appConfig;

    /**
     * 登録
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 認証コードスイッチ
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "ユーザー名を空白にすることはできません";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "ユーザーパスワードを空にすることはできません";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "アカウントの長さは2～20文字でなければなりません";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "パスワードの長さは5～20文字でなければなりません";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "ユーザー保存'" + username + "'失敗しました。登録アカウントは既に存在します";
        }
        else
        {
            sysUser.setUserId(IdUtils.nextIdStr());
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "登録に失敗しました。システム管理者に連絡してください";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 検証コード
     * 
     * @param username ユーザー名
     * @param code コード
     * @param uuid UUID
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = appCache.getCacheObject(verifyKey, String.class);
        appCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
