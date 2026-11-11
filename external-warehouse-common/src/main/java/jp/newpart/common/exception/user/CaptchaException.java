package jp.newpart.common.exception.user;

/**
 * 認証コードエラー異常クラス
 * 
 * @author nextosd
 */
public class CaptchaException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
