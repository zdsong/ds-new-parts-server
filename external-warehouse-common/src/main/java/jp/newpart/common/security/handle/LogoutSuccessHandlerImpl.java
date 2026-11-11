package jp.newpart.common.security.handle;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.manager.AsyncManager;
import jp.newpart.common.manager.factory.AsyncFactory;
import jp.newpart.common.core.service.TokenService;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ServletUtils;
import jp.newpart.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * カスタム終了処理クラスが正常に返されました
 * 
 * @author nextosd
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AppConfig appConfig;

    /**
     * Logout
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // ユーザーキャッシュレコードの削除
            tokenService.delLoginUser(loginUser.getToken());
            // ユーザー終了ログの記録
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), userName, Constants.LOGOUT, MessageUtils.message("I0009")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("I0009"))));
    }
}
