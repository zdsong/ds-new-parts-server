package jp.newpart.externalwarehouse.biz.sys.domain;

/**
 * 現在のオンラインセッション
 * 
 * @author nextosd
 */
public class SysUserOnline
{
    /** Token ID */
    private String tokenId;

    /** ユーザー名 */
    private String userName;

    /** ログインIPアドレス */
    private String ipaddr;

    /** ログインアドレス */
    private String loginLocation;

    /** ブラウザのタイプ */
    private String browser;

    /** オペレーティングシステム */
    private String os;

    /** ログイン時間 */
    private Long loginTime;

    public String getTokenId()
    {
        return tokenId;
    }

    public void setTokenId(String tokenId)
    {
        this.tokenId = tokenId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }
}
