package jp.newpart.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.core.service.TokenService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * tokenフィルタtokenの有効性を検証する
 * 
 * @author nextosd
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        try
        {
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(loginUser))
            {
                // MDCにuserIdを設定
                MDC.put("userId", loginUser.getUsername());
                
                if (StringUtils.isNull(SecurityUtils.getAuthentication()))
                {
                    tokenService.verifyToken(loginUser);
                    
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            chain.doFilter(request, response);
        }
        finally
        {
            // リクエスト処理が完了したらMDCからuserIdを削除
            MDC.remove("userId");
        }
    }
}
