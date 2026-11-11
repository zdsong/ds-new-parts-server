package jp.newpart.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jp.newpart.common.utils.ServletUtils;
import org.springframework.stereotype.Component;

/**
 * サービス関連
 * 
 * @author nextosd
 */
@Component
public class ServerConfig
{
    /**
     * ドメイン名、ポート、コンテキストアクセスパスなど、完全な要求パスを取得する
     * 
     * @return サービスアドレス
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
