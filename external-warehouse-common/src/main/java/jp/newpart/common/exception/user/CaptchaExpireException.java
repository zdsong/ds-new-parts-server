package jp.newpart.common.exception.user;

/**
 * 認証コード失効異常クラス
 * 
 * @author nextosd
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
