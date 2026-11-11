package jp.newpart.common.exception.user;

import jp.newpart.common.exception.base.BaseException;

/**
 * ユーザー情報例外クラス
 * 
 * @author nextosd
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
