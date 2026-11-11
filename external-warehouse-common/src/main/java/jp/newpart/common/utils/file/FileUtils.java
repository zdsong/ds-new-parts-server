package jp.newpart.common.utils.file;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.IdPrefixConstants;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * ファイル処理ツールクラス
 * 
 * @author nextosd
 */
@Slf4j
public class FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 指定したファイルのbyte配列を出力する
     * 
     * @param filePath ファイルパス
     * @param os
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            IOUtils.close(os);
            IOUtils.close(fis);
        }
    }

    /**
     * ファイルへのデータの書き込み
     *
     * @param data データ
     * @return ファイル
     * @throws IOException IO
     */
    public static String writeImportBytes(byte[] data) throws IOException
    {
        return writeBytes(data, AppConfig.getImportPath());
    }

    /**
     * ファイルへのデータの書き込み
     *
     * @param data データ
     * @param uploadDir ファイル
     * @return ファイル
     * @throws IOException IO
     */
    public static String writeBytes(byte[] data, String uploadDir) throws IOException
    {
        FileOutputStream fos = null;
        String pathName = "";
        try
        {
            String extension = getFileExtendName(data);
            pathName = DateUtils.datePath() + "/" + IdUtils.nextIdStr(IdPrefixConstants.UPLOAD_ID)  + "." + extension;
            File file = FileUploadUtils.getAbsoluteFile(uploadDir, pathName);
            fos = new FileOutputStream(file);
            fos.write(data);
        }
        finally
        {
            IOUtils.close(fos);
        }
        return FileUploadUtils.getPathFileName(uploadDir, pathName);
    }

    /**
     * ファイルを削除する
     * 
     * @param filePath ファイル
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);

        if (file.isFile() && file.exists())
        {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * ファイル名の検証
     * 
     * @param filename ファイル名
     * @return
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * ファイルがダウンロード可能かどうかをチェック
     * 
     * @param resource ダウンロードが必要なファイル
     * @return true / false
     */
    public static boolean checkAllowDownload(String resource)
    {
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }

        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource)))
        {
            return true;
        }
        return false;
    }

    /**
     * ダウンロードファイル名の再エンコード
     * 
     * @param request オブジェクト
     * @param fileName ファイル
     * @return エンコードされたファイル名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // Firefox
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // Other
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * ダウンロードファイル名の再エンコード
     *
     * @param response
     * @param realFileName ファイルName
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * パーセント符号化ツールの方法
     *
     * @param s パーセント符号化が必要な文字列
     * @return
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 画像接尾辞の取得
     * 
     * @param photoByte 画像
     * @return
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "gif";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "jpg";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "bmp";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "png";
        }
        return strFileExtendName;
    }

    /**
     * ファイル名の取得 /profile/upload/2025/11/16/farm.png -- farm.png
     * 
     * @param fileName  ファイルパス名
     * @return ファイル名
     */
    public static String getName(String fileName)
    {
        if (fileName == null)
        {
            return null;
        }
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int index = Math.max(lastUnixPos, lastWindowsPos);
        return fileName.substring(index + 1);
    }

    /**
     * 接尾辞なしファイル名の取得 /profile/upload/2025/11/16/farm.png -- farm
     * 
     * @param fileName ファイルパス名
     * @return ファイル名
     */
    public static String getNameNotSuffix(String fileName)
    {
        if (fileName == null)
        {
            return null;
        }
        String baseName = FilenameUtils.getBaseName(fileName);
        return baseName;
    }

    /**
     * CSVファイルのShiftJISをチェックする
     *
     * @param file チェックファイル対象
     * @return  UTF-8 / Shift_JIS
     */
    public static boolean hasShiftJIS(MultipartFile file){
        return FileUtils.isEncoding(file, Constants.SHIFT_JIS);
    }

    /**
     * CSVファイルのUTF-8 BOMをチェックする
     *
     * @param file チェックファイル対象
     * @return true:UTF-8 BOMあり、false:UTF-8 BOMなし
     */
    public static boolean hasUtf8Bom(MultipartFile file) {
        return FileUtils.isEncoding(file, Constants.UTF8);
    }

    /**
     * CSVファイルのUTF-8をチェックする
     *
     * @param file チェックファイル対象
     * @return true:UTF-8
     */
    public static boolean hasUtf8(MultipartFile file) {
        return FileUtils.isEncoding(file, Constants.UTF8);
    }

    /**
     * 指定されたファイルがLF（改行）を使用しているかどうかをチェックします。
     *
     * @param file チェックするファイル
     * @return LFを使用している場合はtrue、それ以外の場合はfalse
     */
    public static boolean  isLf(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                if (ch == '\n') {
                    // LFを検出した場合
                    return true;
                } else if (ch == '\r') {
                    // CRを検出した場合、次の文字をチェック
                    int nextCh = reader.read();
                    if (nextCh != '\n') {
                        // 次の文字がLFでない場合は、CRLFではない
                        return false;
                    } else {
                        // 次の文字がLFである場合は、CRLFであり、LFではない
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 改行符が見つからない場合は、デフォルトでfalseを返す
        return false;
    }

    /**
     * ファイルのエンコーディングを検出する
     * @param file 検出するファイル
     * @return 検出されたエンコーディング名（例: "UTF-8", "Shift_JIS"）またはnull（検出できない場合）
     */
    public static String detectEncoding(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(bytes);

            CharsetMatch match = detector.detect();

            if (match != null) {
                return match.getName();
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ファイルのエンコーディングをチェック
     * @param file チェックするファイル
     * @param encoding チェックするエンコーディング
     */
    public static boolean isEncoding(MultipartFile file, String encoding) {
        String detected = detectEncoding(file);
        return detected != null && detected.equalsIgnoreCase(encoding);
    }
}
