package jp.newpart.common.exception.user;

/**
 * ユーザー認証コードが正しくないか、仕様に一致していない例外クラス
 * 
 * @author nextosd
 */
public class AuthVerifyCodeNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public AuthVerifyCodeNotMatchException()
    {
        super("E0063", null);
    }
}
