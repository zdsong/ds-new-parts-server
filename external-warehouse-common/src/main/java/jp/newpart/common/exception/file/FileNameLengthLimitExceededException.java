package jp.newpart.common.exception.file;

/**
 * ファイル名の最長制限例外クラス
 * 
 * @author nextosd
 */
public class FileNameLengthLimitExceededException extends FileException
{
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength)
    {
        super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
    }
}
