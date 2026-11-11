package jp.newpart.externalwarehouse.biz.sys.service;

import jp.newpart.externalwarehouse.biz.sys.domain.SysUserOnline;
import jp.newpart.common.core.domain.model.LoginUser;

/**
 * オンラインユーザー サービス層
 *
 * @author nextosd
 */
public interface ISysUserOnlineService {
    /**
     * ログインアドレスで情報を検索する
     *
     * @param ipaddr ログインアドレス
     * @param user   ユーザー情報
     * @return オンラインユーザー情報
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * ユーザー名で情報を検索する
     *
     * @param userName ユーザー名
     * @param user     ユーザー情報
     * @return オンラインユーザー情報
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * ログインアドレス/ユーザー名で情報を検索する
     *
     * @param ipaddr   ログインアドレス
     * @param userName ユーザー名
     * @param user     ユーザー情報
     * @return オンラインユーザー情報
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * オンラインユーザー情報を設定する
     *
     * @param user ユーザー情報
     * @return オンラインユーザー
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);
}