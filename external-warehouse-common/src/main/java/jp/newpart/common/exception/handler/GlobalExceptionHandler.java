package jp.newpart.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jp.newpart.common.constant.HttpStatus;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.exception.DemoModeException;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.exception.user.UserPasswordNotMatchException;
import jp.newpart.common.text.Convert;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.html.EscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * グローバル例外プロセッサ
 * 
 * @author nextosd
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 権限チェック例外
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("リクエストアドレス'{}'、権限チェック失敗'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "権限がありません。管理者に権限を与えてください");
    }

    /**
     * リクエスト方法がサポートされていません
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("リクエストアドレス'{}'、'{}'リクエストはサポートされていません", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * ビジネス例外
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        //log.error(e.getMessage());
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * リクエストパスに必要なパス変数がありません
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("リクエストパスに必要なパス変数'{}'が不足し、システム異常が発生しました。", requestURI, e);
        return AjaxResult.error(String.format("リクエストパスに必要なパス変数[%s]がありません", e.getVariableName()));
    }

    /**
     * リクエストパラメータタイプが一致しません
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error("リクエストパラメータタイプが'{}'に一致せず、システム異常が発生する.", requestURI, e);
        return AjaxResult.error(String.format("リクエストパラメータタイプが一致していません。パラメータ[%s]リクエストタイプは'%s'ですが、入力値は'%s'です。", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * ユーザーパスワードが正しくない
     */
    @ExceptionHandler(UserPasswordNotMatchException.class)
    public AjaxResult handleUserPasswordNotMatchException(UserPasswordNotMatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        //log.error("ユーザーパスワードが正しくない", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 未知のランタイム異常をブロック
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("未知のランタイム異常をブロック", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * システム異常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("リクエストアドレス'{}'、システム異常が発生しました。", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * カスタム検証例外
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * カスタム検証例外
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        //log.error(e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * デモモード異常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("デモモード、操作不可");
    }
}
