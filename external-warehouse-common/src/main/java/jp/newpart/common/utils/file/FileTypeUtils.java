package jp.newpart.common.utils.file;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 * ファイルタイプツールクラス
 *
 * @author nextosd
 */
public class FileTypeUtils
{
    /**
     * ファイルタイプの取得
     * <p>
     * 例: farm.txt, 戻る: txt
     * 
     * @param file ファイル名
     * @return 接尾辞("."を含まない)
     */
    public static String getFileType(File file)
    {
        if (null == file)
        {
            return StringUtils.EMPTY;
        }
        return getFileType(file.getName());
    }

    /**
     * ファイルタイプの取得
     * <p>
     * 例: farm.txt, 戻る: txt
     *
     * @param fileName ファイル名
     * @return 接尾辞("."を含まない)
     */
    public static String getFileType(String fileName)
    {
        int separatorIndex = fileName.lastIndexOf(".");
        if (separatorIndex < 0)
        {
            return "";
        }
        return fileName.substring(separatorIndex + 1).toLowerCase();
    }

    /**
     * ファイルタイプの取得
     * 
     * @param photoByte
     * @return 接尾辞("."を含まない)
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "JPG";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "GIF";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "JPG";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "BMP";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }
}