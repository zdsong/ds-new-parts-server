package jp.nasta.pms.common.manager.factory;

import eu.bitwalker.useragentutils.UserAgent;
import jp.nasta.pms.common.constant.Constants;
import jp.nasta.pms.common.core.domain.entity.SysLogininfor;
import jp.nasta.pms.common.core.domain.entity.SysOperLog;
import jp.nasta.pms.common.core.service.ISysLogininforService;
import jp.nasta.pms.common.core.service.ISysOperLogService;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.LogUtils;
import jp.nasta.pms.common.utils.ServletUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.ip.AddressUtils;
import jp.nasta.pms.common.utils.ip.IpUtils;
import jp.nasta.pms.common.utils.spring.SpringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 非同期プラント
 * 
 * @author nextosd
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * ロギングログイン情報
     *
     * @param systemCd システムコード
     * @param username ユーザー名
     * @param status 状態
     * @param message メッセージ
     * @param args リスト
     * @return タスク
     */
    public static TimerTask recordLogininfor(final String systemCd, final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                sys_user_logger.info(s.toString(), args);
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName();
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setInfoId(IdUtils.nextIdStr());
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                logininfor.setSystemCd(systemCd);
                
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作ログ
     * 
     * @param operLog 操作ログ情報
     * @return タスクtask
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                operLog.setOperId(IdUtils.nextIdStr());
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
