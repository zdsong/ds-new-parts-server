package jp.newpart.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

/**
 * 重複提出防止インターセプター
 *
 * @author nextosd
 */
@Component
@Slf4j
public abstract class AbstractRepeatSubmitInterceptor implements HandlerInterceptor {

    private static final String START_TIME_KEY = "requestStartTime";
    private static final String HEADER_ID_KEY = "requestIdKey";

    /**
     * リクエスト処理前に呼び出されるメソッド
     * 重複提出防止のチェックを実施する
     *
     * @param request  HTTPリクエスト
     * @param response HTTPレスポンス
     * @param handler  ハンドラの種類（この場合はHandlerMethod）
     * @return trueの場合、リクエストは次のインターセプターやハンドラに処理される
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String headerIdKey = IdUtils.nextIdStr();
        // リクエストの開始時間を記録
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_KEY, startTime);
        request.setAttribute(HEADER_ID_KEY, headerIdKey);

        StringBuilder requestLog = new StringBuilder(300);
        requestLog.append(StringUtils.format("\n\n================  {} Request Start ================\n", headerIdKey));
        requestLog.append(StringUtils.format("===> {}: {}", request.getMethod(), request.getRequestURI())).append("\n");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);
            requestLog.append(StringUtils.format("===Headers===  {} : {}", headerName, headerValue)).append("\n");
        }
        // リクエストの詳細情報をログに出力
        log.info(requestLog.toString());

        return true;
    }

    /**
     * リクエスト処理が終了した後に呼び出されるメソッド
     * ログ出力などの後処理に使用される
     *
     * @param request  HTTPリクエスト
     * @param response HTTPレスポンス
     * @param handler  ハンドラの種類
     * @param ex       例外情報（発生した場合）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // リクエストの開始時間を取得
        Long startTime = (Long) request.getAttribute(START_TIME_KEY);
        String headerIdKey = (String) request.getAttribute(HEADER_ID_KEY);

        StringBuilder requestLog = new StringBuilder(300);
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            requestLog.append(StringUtils.format("\n================ {} Request End  - Duration: {} ms ================\n", headerIdKey, duration));
            log.info(requestLog.toString());
            if (ex != null) {
                log.error("End: {} {} {} - Duration: {} ms - Exception: {}",
                        headerIdKey, request.getMethod(), request.getRequestURI(), duration, ex.getMessage(), ex);
            }
        }
    }
}
