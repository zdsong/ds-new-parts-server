package jp.newpart.common.exception.file;

import jp.newpart.common.exception.base.BaseException;

/**
 * ファイル情報例外クラス
 * 
 * @author nextosd
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
