package jp.newpart.common.exception.user;

/**
 * ブラックリストIP異常クラス
 * 
 * @author nextosd
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
