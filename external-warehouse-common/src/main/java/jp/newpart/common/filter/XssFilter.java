package jp.newpart.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * XSS攻撃を防止するフィルター
 *
 * @author nextosd
 */
public class XssFilter implements Filter {
    /**
     * 除外リンク
     */
    private List<String> excludes = Collections.synchronizedList(new ArrayList<>());

    /**
     * フィルターの初期化メソッド
     * コンフィグレーションパラメータから除外URLを読み取り、初期設定を行う
     *
     * @param filterConfig フィルターのコンフィグレーション情報
     * @throws ServletException サーブレット例外
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(tempExcludes)) {
            String[] urls = tempExcludes.split(",");
            for (String url : urls) {
                excludes.add(url.trim());
            }
        }
    }

    /**
     * フィルタリングの実際の処理を行うメソッド
     * 指定されたURLが除外リストに含まれているか、またはGETやDELETEメソッドの場合、
     * そのままリクエストを先に進める。それ以外の場合、XSSフィルタリングを適用する
     *
     * @param request  クライアントからのリクエスト
     * @param response クライアントへのレスポンス
     * @param chain    フィルターチェーン
     * @throws IOException      入出力例外
     * @throws ServletException サーブレット例外
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeUrl(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 除外URLまたはメソッドを処理するメソッド
     * リクエストがGETまたはDELETEメソッドであるか、または除外リストに含まれているURLの場合、
     * フィルタリングをスキップする
     *
     * @param request  クライアントからのリクエスト
     * @param response クライアントへのレスポンス
     * @return true - フィルタリングをスキップする場合、false - フィルタリングを適用する場合
     */
    private boolean handleExcludeUrl(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getServletPath();
        String method = request.getMethod();
        if (method == null || "get".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
            return true;
        }
        return StringUtils.matches(url, excludes);
    }

    /**
     * フィルターの破棄メソッド
     * 現在は実装されていないが、将来フィルターが破棄される際にリソースを解放するための場所として使用される
     */
    @Override
    public void destroy() {
    }
}