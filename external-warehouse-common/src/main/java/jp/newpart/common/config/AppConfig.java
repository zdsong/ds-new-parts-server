package jp.newpart.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * プロジェクト関連構成の読み取り
 * 
 * @author nextosd
 */
@Component
@ConfigurationProperties(prefix = "external-warehouse")
public class AppConfig
{
    /** プロジェクト名 */
    private String name;

    /** Version */
    private String version;

    /** 著作権年 */
    private String copyrightYear;

    /**
     * キャッシュ: redis/db
     */
    private static String cache;

    /** アップロードパス */
    private static String profile;

    /** アドレス取得スイッチ */
    private static boolean addressEnabled;

    /** 認証コードタイプ */
    private static String captchaType;
    /**
     * 暗号化に使用する秘密鍵（16バイト）
     */
    private static String decryptFieldKey;
    /**
     * 初期化ベクトル（IV、16バイト）
     */
    private static String decryptFieldIv;
    /**
     * ホワイトURLリスト
     */
    private static List<String> whiteUrlList;
    /**
     * Mail Enabled
     */
    private static Boolean mailEnabled;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        AppConfig.profile = profile;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        AppConfig.cache = cache;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        AppConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        AppConfig.captchaType = captchaType;
    }


    public static String getDecryptFieldIv() {
        return decryptFieldIv;
    }

    public void setDecryptFieldIv(String decryptFieldIv) {
        AppConfig.decryptFieldIv = decryptFieldIv;
    }

    public static String getDecryptFieldKey() {
        return decryptFieldKey;
    }

    public void setDecryptFieldKey(String decryptFieldKey) {
        AppConfig.decryptFieldKey = decryptFieldKey;
    }

    public static List<String> getWhiteUrlList() {
        return whiteUrlList;
    }

    public void setWhiteUrlList(List<String> whiteUrlList) {
        AppConfig.whiteUrlList = whiteUrlList;
    }

    public static Boolean isMailEnabled() {
        return mailEnabled;
    }

    public void setMailEnabled(Boolean mailEnabled) {
        AppConfig.mailEnabled = mailEnabled;
    }

    /**
     * インポートアップロードパスの取得
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * アバターアップロードパスの取得
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * ダウンロードパスの取得
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * アップロードパスの取得
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
