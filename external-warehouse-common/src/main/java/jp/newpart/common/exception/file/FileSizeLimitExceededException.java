package jp.newpart.common.exception.file;

/**
 * ファイル名サイズ制限例外クラス
 * 
 * @author nextosd
 */
public class FileSizeLimitExceededException extends FileException
{
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize)
    {
        super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
    }
}
