package jp.newpart.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * システムアクセスログテーブル sys_logininfor
 * 
 * @author nextosd
 */
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "番号", cellType = Excel.ColumnType.STRING)
    private String infoId;

    /** ユーザーアカウント */
    @Excel(name = "ユーザーアカウント")
    private String userName;

    /** ログインステータス 0=成功、1=失敗 */
    @Excel(name = "ログインステータス", readConverterExp = "0=成功,1=失敗")
    private String status;

    /** ログインIPアドレス */
    @Excel(name = "ログインIPアドレス")
    private String ipaddr;

    /** ログイン場所 */
    @Excel(name = "ログイン場所")
    private String loginLocation;

    /** ブラウザ */
    @Excel(name = "ブラウザ")
    private String browser;

    /** オペレーティングシステム */
    @Excel(name = "オペレーティングシステム")
    private String os;

    /** ヒントメッセージ */
    @Excel(name = "メッセージ")
    private String msg;

    /** アクセス時間 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "アクセス時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /** システムコード */
    @Excel(name = "システムコード")
    private String systemCd;

    /**
     * ログイン開始時間
     */
    private String beginTime;

    /**
     * ログイン終了時間
     */
    private String endTime;

    public String getInfoId()
    {
        return infoId;
    }

    public void setInfoId(String infoId)
    {
        this.infoId = infoId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }

    public String getSystemCd() {
        return systemCd;
    }

    public void setSystemCd(String systemCd) {
        this.systemCd = systemCd;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
