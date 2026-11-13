package jp.newpart.externalwarehouse.sys.service.impl;

import jp.newpart.externalwarehouse.sys.domain.SysUserOnline;
import jp.newpart.externalwarehouse.sys.service.ISysUserOnlineService;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.utils.StringUtils;
import org.springframework.stereotype.Service;

/**
 * オンラインユーザー サービス層処理
 *
 * @author nextosd
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    /**
     * ログインアドレスを介して情報を検索する
     *
     * @param ipaddr ログインアドレス
     * @param user   ユーザー情報
     * @return オンラインユーザー情報
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * ユーザー名を介して情報を検索する
     *
     * @param userName ユーザー名
     * @param user     ユーザー情報
     * @return オンラインユーザー情報
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * ログインアドレス/ユーザー名を介して情報を検索する
     *
     * @param ipaddr   ログインアドレス
     * @param userName ユーザー名
     * @param user     ユーザー情報
     * @return オンラインユーザー情報
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * オンラインユーザー情報を設定する
     *
     * @param user ユーザー情報
     * @return オンラインユーザー
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUsername())) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());

        return sysUserOnline;
    }
}