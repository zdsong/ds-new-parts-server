package jp.newpart.common.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AWSユーティリティクラス
 * 一般的なAWS関連の補助機能を提供
 * 
 * @author nextosd
 */
public class AwsUtils {
    
    private static final Logger log = LoggerFactory.getLogger(AwsUtils.class);
    
    /**
     * ユニークなファイル名を生成
     * 
     * @param originalFilename 元のファイル名
     * @return ユニークなファイル名
     */
    public static String generateUniqueFileName(String originalFilename) {
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        return timestamp + "_" + uuid + fileExtension;
    }
    
    /**
     * S3ストレージキーを構築
     * 
     * @param pathPrefix パスプレフィックス
     * @param fileName ファイル名
     * @return S3キー
     */
    public static String buildS3Key(String pathPrefix, String fileName) {
        if (pathPrefix != null && !pathPrefix.trim().isEmpty()) {
            return pathPrefix.endsWith("/") ? pathPrefix + fileName : pathPrefix + "/" + fileName;
        }
        return fileName;
    }
    
    /**
     * ファイルサイズが制限範囲内か検証
     * 
     * @param fileSize ファイルサイズ（バイト）
     * @param maxSizeMB 最大サイズ（MB）
     * @return 有効かどうか
     */
    public static boolean validateFileSize(long fileSize, long maxSizeMB) {
        return fileSize <= maxSizeMB * 1024 * 1024;
    }
    
    /**
     * ファイルサイズ制限のエラーメッセージを取得
     * 
     * @param maxSizeMB 最大サイズ（MB）
     * @return エラーメッセージ
     */
    public static String getFileSizeLimitMessage(long maxSizeMB) {
        return "ファイルサイズが制限を超えています: " + maxSizeMB + "MB";
    }
    
    /**
     * ファイルサイズをフォーマット（ログ表示用）
     * 
     * @param size ファイルサイズ（バイト）
     * @return フォーマットされた文字列
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}