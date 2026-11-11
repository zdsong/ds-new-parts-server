package jp.newpart.common.exception.user;

/**
 * ユーザーパスワードが正しくないか、仕様に一致していない例外クラス
 * 
 * @author nextosd
 */
public class UserPasswordNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super("E0004", null);
    }
}
