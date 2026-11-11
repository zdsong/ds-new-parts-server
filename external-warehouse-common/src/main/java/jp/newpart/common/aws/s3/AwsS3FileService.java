package jp.newpart.common.aws.s3;

import jp.newpart.common.aws.AwsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * AWS S3ファイルサービス実装
 * 
 * @author nextosd
 */
@Service
public class AwsS3FileService {
    
    private static final Logger log = LoggerFactory.getLogger(AwsS3FileService.class);
    
    @Autowired
    private S3Client s3Client;
    
    @Autowired
    private AwsS3Config awsS3Config;
    
    /**
     * ファイルをS3にアップロード（ファイルパス指定）
     * 
     * @param s3Key S3オブジェクトキー
     * @param filePath ローカルファイルパス
     * @return アップロード成功時はtrueを返す
     */
    public boolean uploadFile(String s3Key, String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                log.error("ファイルが存在しません: {}", filePath);
                return false;
            }
            
            // ファイルサイズをチェック
            if (!AwsUtils.validateFileSize(file.length(), awsS3Config.getMaxFileSize())) {
                throw new IllegalArgumentException(AwsUtils.getFileSizeLimitMessage(awsS3Config.getMaxFileSize()));
            }
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .contentLength(file.length())
                    .contentDisposition("attachment; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"))
                    .build();
            
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            
            log.info("ファイルアップロード成功 - ファイル名: {}, S3 Key: {}, サイズ: {}", 
                    file.getName(), s3Key, AwsUtils.formatFileSize(file.length()));
            
            return true;
            
        } catch (java.io.IOException e) {
            log.error("ファイルアップロード失敗 - ファイルパス: {}, エラー: {}", filePath, e.getMessage(), e);
            return false;
        } catch (software.amazon.awssdk.core.exception.SdkException e) {
            log.error("S3アップロード失敗 - ファイルパス: {}, エラー: {}", filePath, e.getMessage(), e);
            return false;
        }
    }

    /**
     * ファイルをS3にアップロード
     * 
     * @param file アップロードするファイル
     * @return ファイルのS3キー
     */
    public String uploadFile(MultipartFile file) {
        try {
            // ファイルサイズをチェック
            if (!AwsUtils.validateFileSize(file.getSize(), awsS3Config.getMaxFileSize())) {
                throw new IllegalArgumentException(AwsUtils.getFileSizeLimitMessage(awsS3Config.getMaxFileSize()));
            }
            
            // ユニークなファイル名を生成
            String originalFilename = file.getOriginalFilename();
            String fileName = AwsUtils.generateUniqueFileName(originalFilename);
            String s3Key = AwsUtils.buildS3Key(awsS3Config.getPathPrefix(), fileName);
            
            // ファイルをS3にアップロード
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .contentDisposition("attachment; filename=" + java.net.URLEncoder.encode(originalFilename, "UTF-8"))
                    .build();
            
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            log.info("ファイルアップロード成功 - ファイル名: {}, S3 Key: {}, サイズ: {}", 
                    originalFilename, s3Key, AwsUtils.formatFileSize(file.getSize()));
            
            return s3Key;
            
        } catch (IOException e) {
            log.error("ファイルアップロード失敗 - ファイル名: {}, エラー: {}", file.getOriginalFilename(), e.getMessage(), e);
            throw new RuntimeException("ファイルアップロード失敗", e);
        } catch (SdkException e) {
            log.error("S3アップロード失敗 - ファイル名: {}, エラー: {}", file.getOriginalFilename(), e.getMessage(), e);
            throw new RuntimeException("S3アップロード失敗", e);
        }
    }
    
    /**
     * S3内のファイルを削除
     * 
     * @param s3Key ファイルのS3キー
     * @return 削除が成功したかどうか
     */
    public boolean deleteFile(String s3Key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            s3Client.deleteObject(deleteObjectRequest);
            
            log.info("ファイル削除成功 - S3 Key: {}", s3Key);
            return true;
            
        } catch (SdkException e) {
            log.error("ファイル削除失敗 - S3 Key: {}, エラー: {}", s3Key, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * ファイルが存在するかチェック
     * 
     * @param s3Key ファイルのS3キー
     * @return 存在するかどうか
     */
    public boolean fileExists(String s3Key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            s3Client.headObject(headObjectRequest);
            return true;
            
        } catch (NoSuchKeyException e) {
            return false;
        } catch (SdkException e) {
            log.error("ファイル存在チェック失敗 - S3 Key: {}, エラー: {}", s3Key, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * ファイルダウンロードURLを取得
     * 
     * @param s3Key ファイルのS3キー
     * @return ダウンロードURL
     */
    public String getFileDownloadUrl(String s3Key) {
        try {
            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            return s3Client.utilities().getUrl(getUrlRequest).toString();
            
        } catch (SdkException e) {
            log.error("ファイルダウンロードURL取得失敗 - S3 Key: {}, エラー: {}", s3Key, e.getMessage(), e);
            throw new RuntimeException("ファイルダウンロードURL取得失敗", e);
        }
    }
    
    /**
     * ファイルの事前署名付きURLを取得（有効期限付き）
     * 
     * @param s3Key ファイルのS3キー
     * @param expirationMinutes 有効期限（分）
     * @return 事前署名付きURL
     */
    public String getPresignedUrl(String s3Key, int expirationMinutes) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            // Presignerを使用して事前署名付きURLを生成
            return s3Client.utilities()
                    .getUrl(GetUrlRequest.builder()
                            .bucket(awsS3Config.getBucketName())
                            .key(s3Key)
                            .build())
                    .toString();
            
        } catch (SdkException e) {
            log.error("事前署名付きURL取得失敗 - S3 Key: {}, エラー: {}", s3Key, e.getMessage(), e);
            throw new RuntimeException("事前署名付きURL取得失敗", e);
        }
    }
    
    /**
     * ファイルをダウンロード
     * 
     * @param s3Key S3内のファイルキー
     * @return ファイル入力ストリーム
     */
    public InputStream downloadFile(String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsS3Config.getBucketName())
                    .key(s3Key)
                    .build();
            
            return s3Client.getObject(getObjectRequest);
            
        } catch (SdkException e) {
            log.error("ファイルダウンロード失敗 - S3 Key: {}, エラー: {}", s3Key, e.getMessage(), e);
            throw new RuntimeException("ファイルダウンロード失敗", e);
        }
    }
    
    /**
     * S3ファイルの内容を読み取り、ログに記録
     * 
     * @param fileName ファイル名
     * @param s3KeyPrefix S3キープレフィックス（デフォルトはuploads/）
     * @return 読み取り結果情報
     */
    public S3FileReadResult readFileContent(String fileName, String s3KeyPrefix) {
        InputStream inputStream = null;
        try {
            // ファイルタイプをチェック
            if (!fileName.toLowerCase().endsWith(".csv") && !fileName.toLowerCase().endsWith(".txt")) {
                return S3FileReadResult.error("CSVとTXTファイルのみサポートされています");
            }
            
            // S3キーを構築
            String prefix = s3KeyPrefix != null ? s3KeyPrefix : "uploads/";
            String s3Key = prefix + fileName;
            
            // ファイルが存在するかチェック
            if (!fileExists(s3Key)) {
                return S3FileReadResult.error("ファイルが存在しません: " + fileName);
            }
            
            // ファイル内容をダウンロード
            inputStream = downloadFile(s3Key);
            
            // ファイルサイズを制限（最大10MB）
            byte[] buffer = new byte[1024 * 1024 * 10]; // 10MBバッファ
            int bytesRead = inputStream.read(buffer);
            
            if (bytesRead == -1) {
                return S3FileReadResult.error("ファイルが空です: " + fileName);
            }
            
            // 実際に読み取ったバイトのみを読み取る
            String content = new String(buffer, 0, bytesRead, "UTF-8");
            
            // ファイル内容をバックエンドログに記録
            log.info("=== S3ファイル内容開始 ===");
            log.info("ファイル名: {}", fileName);
            log.info("ファイルサイズ: {} bytes", bytesRead);
            log.info("S3 Key: {}", s3Key);
            log.info("--- ファイル内容 ---");
            log.info(content);
            log.info("=== S3ファイル内容終了 ===");
            
            return S3FileReadResult.success(fileName, s3Key, bytesRead, "ファイル内容がバックエンドログに記録されました");
            
        } catch (Exception e) {
            log.error("S3ファイル読み取り失敗 - ファイル名: {}, エラー: {}", fileName, e.getMessage(), e);
            return S3FileReadResult.error("S3ファイル読み取り失敗: " + e.getMessage());
        } finally {
            // 入力ストリームが正しく閉じられることを確認
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("入力ストリームのクローズ中にエラーが発生しました: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * S3ファイルの内容を読み取り（デフォルトプレフィックスを使用）
     * 
     * @param fileName ファイル名
     * @return 読み取り結果情報
     */
    public S3FileReadResult readFileContent(String fileName) {
        return readFileContent(fileName, null);
    }
    
    /**
     * S3ファイル読み取り結果カプセル化クラス（内部静的クラス）
     */
    public static class S3FileReadResult {
        
        private boolean success;
        private String message;
        private String fileName;
        private String s3Key;
        private Integer fileSize;
        
        private S3FileReadResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        private S3FileReadResult(boolean success, String message, String fileName, String s3Key, Integer fileSize) {
            this.success = success;
            this.message = message;
            this.fileName = fileName;
            this.s3Key = s3Key;
            this.fileSize = fileSize;
        }
        
        /**
         * 成功結果を作成
         */
        public static S3FileReadResult success(String fileName, String s3Key, Integer fileSize, String message) {
            return new S3FileReadResult(true, message, fileName, s3Key, fileSize);
        }
        
        /**
         * エラー結果を作成
         */
        public static S3FileReadResult error(String message) {
            return new S3FileReadResult(false, message);
        }
        
        // Getterメソッド
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getFileName() {
            return fileName;
        }
        
        public String getS3Key() {
            return s3Key;
        }
        
        public Integer getFileSize() {
            return fileSize;
        }
    }
    
}