package jp.newpart.externalwarehouse.biz.domain.vo;

import jp.newpart.common.annotation.EncryptedField;
import lombok.Data;

/**
 * 顧客マスタ詳細情報Vo
 *
 * @author nextosd
 */
@Data
public class MastCustomerVo {

    /**
     * ID
     */
    private String id;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 顧客名
     */
    @EncryptedField(autoEncrypt = false)
    private String customerName;

    /**
     * 電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String phoneNumber;

    /**
     * 郵便番号
     */
    @EncryptedField(autoEncrypt = false)
    private String postalCode;

    /**
     * 都道府県
     */
    private String kenName;

    /**
     * 市区町村
     */
    private String cityName;

    /**
     * 町域・番地
     */
    @EncryptedField(autoEncrypt = false)
    private String townName;

    /**
     * ビル等
     */
    @EncryptedField(autoEncrypt = false)
    private String buildingEtc;

    /**
     * 住所
     */
    @EncryptedField(autoEncrypt = false)
    private String address;

    /**
     * メールアドレス
     */
    @EncryptedField(autoEncrypt = false)
    private String mailAddress;

    /**
     * 会員ID
     */
    private String memberId;
}