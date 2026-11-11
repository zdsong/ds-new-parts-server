package jp.newpart.common.exception.user;

/**
 * ユーザエラー最大回数異常クラス
 * 
 * @author nextosd
 */
public class UserPasswordRetryLimitExceedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime)
    {
        super("E0053", new Object[] { retryLimitCount, lockTime });
    }
}
