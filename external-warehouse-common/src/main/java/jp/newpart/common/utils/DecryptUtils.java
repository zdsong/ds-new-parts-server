package jp.newpart.common.utils;


import jp.newpart.common.config.AppConfig;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * デコードユーティリティクラス
 * <p>
 * このクラスは、AES/CBC/PKCS5Padding方式を使用して暗号化されたテキストを復号するためのユーティリティです。
 *
 * @author nextosd
 */
@Slf4j
public class DecryptUtils {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * AES 暗号化
     */
    public static String encryptAES(String data) {
        if(ValueUtils.isEmpty(data)){
            return data;
        }
        try {
            byte[] keyBytes = hexStringToByteArray(AppConfig.getDecryptFieldKey());
            byte[] ivBytes = hexStringToByteArray(AppConfig.getDecryptFieldIv());

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(Constants.UTF8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // 例外が発生した場合、スタックトレースを出力し、空文字列を返す
            log.error("decryptAES error: {} {}", data, e.getMessage());
            throw new ServiceException(MessageUtils.message("EM007"));
        }
    }

    /**
     * AES 暗号化されたテキストを復号します。
     *
     */
    public static String decryptAES(String encryptedData) {
        if(ValueUtils.isEmpty(encryptedData)){
            return encryptedData;
        }
        try {
            encryptedData = encryptedData.replaceAll("\\s", "");
            byte[] keyBytes = hexStringToByteArray(AppConfig.getDecryptFieldKey());
            byte[] ivBytes = hexStringToByteArray(AppConfig.getDecryptFieldIv());

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, Constants.UTF8);
        } catch (Exception e) {
            // 例外が発生した場合、スタックトレースを出力し、空文字列を返す
            log.error("decryptAES error: {} {}", encryptedData, e.getMessage());
            throw new ServiceException(MessageUtils.message("EM008"));
        }
    }

    /**
     * 暗号化されたテキストを復号します。
     *
     * @param encryptedText Base64形式でエンコードされた暗号化テキスト
     * @return 復号された平文文字列
     * <p>
     * このメソッドは以下の手順で復号を行います：
     * 1. 指定された暗号化テキストをBase64デコードします。
     * 2. AES/CBC/PKCS5Padding方式を使用して復号処理を実行します。
     * 3. 復号結果をUTF-8文字列として返します。
     * <p>
     * 注意：復号中に例外が発生した場合、スタックトレースを出力し、空文字列を返します。
     */
    public static String decryptField(String encryptedText) {
        try {
            if (StringUtils.isEmpty(encryptedText)) {
                return encryptedText;
            }

            return encryptedText;
            //return decryptAES(encryptedText);

        } catch (Exception e) {
            // 例外が発生した場合、スタックトレースを出力し、空文字列を返す
            e.printStackTrace();
            log.error("decryptField error: {} {}", encryptedText, e.getMessage());
            return encryptedText;
        }
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args){
        String s = "59KDbersTvuTdyh8uklt158e9utyh9s7";
        String iv = "q3KanypB7RB5Rqom32tdzyaecgeq938u";
       // decryptAES(s);
       // byte[] bytes = hexStringToByteArray(s);
       // byte[] bytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        //System.out.println(bytes.length);
        //System.out.println(bytes.toString());
        //byte[] ivbytes = hexStringToByteArray(iv);
        //System.out.println(ivbytes.length);
        //System.out.println("Key bytes (hex): " + bytesToHex(bytes));
        //System.out.println("IV bytes (hex): " + bytesToHex(ivbytes));
   

    }
}