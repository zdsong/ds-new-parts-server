package jp.newpart.common.annotation;

import java.lang.annotation.*;

/**
 * デコードフィールド
 *
 * @author nextosd
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptedField {
    /**
     * アルゴリズムの種類。デフォルトはAES。
     */
    String algorithm() default "AES";

    /**
     * データベースクエリ中に自動的に復号化しますか
     */
    boolean autoDecrypt() default true;

    /**
     * 保存時に自動的に暗号化しますか
     */
    boolean autoEncrypt() default true;
}