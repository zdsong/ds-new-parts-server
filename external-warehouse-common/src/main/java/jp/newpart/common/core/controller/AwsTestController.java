package jp.newpart.common.core.controller;

import jp.newpart.common.aws.s3.AwsS3FileService;
import jp.newpart.common.aws.ses.AwsSesEmailService;
import jp.newpart.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AWS機能テストコントローラ
 * 
 * @author nextosd
 */
@RestController
@RequestMapping("/api/aws/test")
public class AwsTestController {
    
    private static final Logger log = LoggerFactory.getLogger(AwsTestController.class);
    
    @Autowired
    private AwsS3FileService awsS3FileService;
    
    @Autowired
    private AwsSesEmailService awsSesEmailService;
    
    /**
     * S3ファイルアップロードテスト
     * 
     * @param file アップロードするファイル
     * @return アップロード結果
     */
    @PostMapping("/s3/upload")
    public AjaxResult testS3Upload(@RequestParam("file") MultipartFile file) {
        try {
            String s3Key = awsS3FileService.uploadFile(file);
            return AjaxResult.success("ファイルアップロード成功", s3Key);
        } catch (Exception e) {
            log.error("S3ファイルアップロード失敗", e);
            return AjaxResult.error("ファイルアップロード失敗: " + e.getMessage());
        }
    }
    
    /**
     * S3ファイル存在チェックテスト
     * 
     * @param s3Key ファイルのS3キー
     * @return 存在チェック結果
     */
    @GetMapping("/s3/exists")
    public AjaxResult testS3FileExists(@RequestParam("s3Key") String s3Key) {
        try {
            boolean exists = awsS3FileService.fileExists(s3Key);
            return AjaxResult.success("ファイル存在チェック成功", exists ? "ファイルが存在します" : "ファイルが存在しません");
        } catch (Exception e) {
            log.error("S3ファイル存在チェック失敗", e);
            return AjaxResult.error("ファイル存在チェック失敗: " + e.getMessage());
        }
    }
    
    /**
     * S3ファイル削除テスト
     * 
     * @param s3Key ファイルのS3キー
     * @return 削除結果
     */
    @DeleteMapping("/s3/delete")
    public AjaxResult testS3DeleteFile(@RequestParam("s3Key") String s3Key) {
        try {
            boolean success = awsS3FileService.deleteFile(s3Key);
            return AjaxResult.success("ファイル削除" + (success ? "成功" : "失敗"));
        } catch (Exception e) {
            log.error("S3ファイル削除失敗", e);
            return AjaxResult.error("ファイル削除失敗: " + e.getMessage());
        }
    }
    
    /**
     * SESシンプルメール送信テスト
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param content メール内容
     * @return 送信結果
     */
    @PostMapping("/ses/send-simple")
    public AjaxResult testSesSendSimpleEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content) {
        try {
            boolean success = awsSesEmailService.sendSimpleEmail(to, null,null,subject, content);
            return AjaxResult.success("メール送信" + (success ? "成功" : "失敗"));
        } catch (Exception e) {
            log.error("SESシンプルメール送信失敗", e);
            return AjaxResult.error("メール送信失敗: " + e.getMessage());
        }
    }
    
    /**
     * SES HTMLメール送信テスト
     * 
     * @param to 受信者メールアドレス
     * @param subject メール件名
     * @param htmlContent HTMLメール内容
     * @return 送信結果
     */
    @PostMapping("/ses/send-html")
    public AjaxResult testSesSendHtmlEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("htmlContent") String htmlContent) {
        try {
            boolean success = awsSesEmailService.sendHtmlEmail(to, null,null,subject, htmlContent);
            return AjaxResult.success("HTMLメール送信" + (success ? "成功" : "失敗"));
        } catch (Exception e) {
            log.error("SES HTMLメール送信失敗", e);
            return AjaxResult.error("HTMLメール送信失敗: " + e.getMessage());
        }
    }
    
    /**
     * SESユーザー登録メール送信テスト
     * 
     * @param to 受信者メールアドレス
     * @param username ユーザー名
     * @return 送信結果
     */
    @PostMapping("/ses/send-registration")
    public AjaxResult testSesSendRegistrationEmail(
            @RequestParam("to") String to,
            @RequestParam("username") String username) {
        try {
            boolean success = awsSesEmailService.sendUserRegistrationEmail(to, username);
            return AjaxResult.success("ユーザー登録メール送信" + (success ? "成功" : "失敗"));
        } catch (Exception e) {
            log.error("SESユーザー登録メール送信失敗", e);
            return AjaxResult.error("ユーザー登録メール送信失敗: " + e.getMessage());
        }
    }
    
    /**
     * S3ファイル内容読み取りテスト
     * 
     * @param fileName ファイル名
     * @param s3KeyPrefix S3キープレフィックス（オプション）
     * @return 読み取り結果
     */
    @GetMapping("/s3/read")
    public AjaxResult testS3FileReadResult(
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "s3KeyPrefix", required = false) String s3KeyPrefix) {
        try {
            AwsS3FileService.S3FileReadResult readResult = awsS3FileService.readFileContent(fileName, s3KeyPrefix);
            if (readResult.isSuccess()) {
                return AjaxResult.success("ファイル内容読み取り成功", readResult);
            } else {
                return AjaxResult.error("ファイル内容読み取り失敗: " + readResult.getMessage());
            }
        } catch (Exception e) {
            log.error("S3ファイル内容読み取り失敗", e);
            return AjaxResult.error("ファイル内容読み取り失敗: " + e.getMessage());
        }
    }
}