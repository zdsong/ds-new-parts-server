package jp.newpart.common.security.handle;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.constant.HttpStatus;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * 認証失敗処理クラスが不正な権限を返す
 * 
 * @author nextosd
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        int code = HttpStatus.UNAUTHORIZED;
        //String msg = StringUtils.format("リクエストアクセス:{}、認証に失敗しました、システムリソースにアクセスできません", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, MessageUtils.message("EM011"))));
    }
}
