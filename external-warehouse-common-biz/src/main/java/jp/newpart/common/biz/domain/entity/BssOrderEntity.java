package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 注文情報 (bss_order)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssOrderEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文情報ID
     */
    private String id;

    /**
     * 注文番号
     */
    private String orderId;

    /**
     * システム注文番号
     */
    private String sysOrderId;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * ステータス
     */
    private String status;

    /**
     * 注文日時
     */
    private String orderTime;

    /**
     * 集荷予定日
     */
    private String deliveryPlanDate;

    /**
     * 決済方法
     */
    private String paymentType;

    /**
     * 決済日
     */
    private String paymentDate;

    /**
     * 出荷予定日
     */
    private String shipPlanDate;

    /**
     * 出荷日
     */
    private String shipDate;

    /**
     * 配送完了日
     */
    private String deliveryDate;

    /**
     * システム顧客番号
     */
    private String sysCustomerId;

    /**
     * 会員ID
     */
    private String memberId;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 注文者
     */
    @EncryptedField
    private String customerName;

    /**
     * 氏名（姓）
     */
    @EncryptedField
    private String firstName;

    /**
     * 氏名（名）
     */
    @EncryptedField
    private String lastName;

    /**
     * 氏名フリガナ（姓）
     */
    @EncryptedField
    private String furiganaSei;

    /**
     * 氏名フリガナ（名）
     */
    @EncryptedField
    private String furiganaMei;

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
    @EncryptedField
    private String townName;

    /**
     * ビル等
     */
    @EncryptedField
    private String buildingEtc;

    /**
     * 会社名
     */
    @EncryptedField
    private String companyName;

    /**
     * 部署名
     */
    @EncryptedField
    private String departmentName;

    /**
     * メールアドレス
     */
    @EncryptedField
    private String mailAddress;

    /**
     * 電話番号
     */
    @EncryptedField
    private String phoneNumber;

    /**
     * 郵便番号
     */
    @EncryptedField
    private String postalCode;

    /**
     * 携帯電話番号
     */
    @EncryptedField
    private String mobile;

    /**
     * FAX番号
     */
    @EncryptedField
    private String fax;

    /**
     * 性別
     */
    private String gender;

    /**
     * 生年月日
     */
    @EncryptedField
    private String birth;

    /**
     * 備考
     */
    private String memo;

    /**
     * フリー項目１
     */
    private String freeItem1;

    /**
     * フリー項目２
     */
    private String freeItem2;

    /**
     * フリー項目３
     */
    private String freeItem3;

    /**
     * 住所
     */
    @EncryptedField
    private String address;

    /**
     * 顧客メモ
     */
    private String customerMemo;

    /**
     * 商品合計金額（税込）
     */
    private BigDecimal itemTotalTax;

    /**
     * 商品合計金額
     */
    private BigDecimal itemTotal;

    /**
     * Version
     */
    private Integer version;

}
