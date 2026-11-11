package jp.newpart.common.exception.user;

/**
 * ユーザーに例外クラスは存在しません
 * 
 * @author nextosd
 */
public class UserNotExistsException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserNotExistsException()
    {
        super("E0050", null);
    }
}
