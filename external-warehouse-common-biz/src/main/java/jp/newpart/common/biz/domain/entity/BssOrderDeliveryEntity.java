package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 注文配送先情報 (bss_order_delivery)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssOrderDeliveryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文配送先情報ID
     */
    private String id;

    /**
     * 注文番号
     */
    private String orderId;

    /**
     * 届け先名称
     */
    @EncryptedField
    private String deliveryCustomerName;

    /**
     * 配送先選択
     */
    private String deliverySelection;

    /**
     * 配送先選択記述内容
     */
    private String deliverySelectionDesc;

    /**
     * 配送先氏名（姓）
     */
    @EncryptedField
    private String deliveryFirstName;

    /**
     * 配送先氏名（名）
     */
    @EncryptedField
    private String deliveryLastName;

    /**
     * 配送先氏名フリガナ（姓）
     */
    @EncryptedField
    private String deliveryFuriganaSei;

    /**
     * 配送先氏名フリガナ（名）
     */
    @EncryptedField
    private String deliveryFuriganaMei;

    /**
     * 配送先郵便番号
     */
    @EncryptedField
    private String deliveryPostalCode;

    /**
     * 配送先都道府県
     */
    private String deliveryKenName;

    /**
     * 配送先市区町村
     */
    private String deliveryCityName;

    /**
     * 配送先町域・番地
     */
    @EncryptedField
    private String deliveryTownName;

    /**
     * 配送先ビル等
     */
    @EncryptedField
    private String deliveryBuildingEtc;

    /**
     * 配送先会社名
     */
    @EncryptedField
    private String deliveryCompanyName;

    /**
     * 配送先部署名
     */
    @EncryptedField
    private String deliveryDepartmentName;

    /**
     * 配送方法番号
     */
    private String deliveryMethodNo;

    /**
     * 配送方法
     */
    private String deliveryMethod;

    /**
     * 送り状番号
     */
    private String shippingSlipNumber;

    /**
     * 途中終了区分
     */
    private String midwayTerminationKbn;

    /**
     * 次回配送日
     */
    private String nextDeliveryDate;

    /**
     * 配送サイクル種別
     */
    private String deliveryCycleType;

    /**
     * 配送サイクル日数
     */
    private String deliveryCycleDays;

    /**
     * 運送依頼番号
     */
    private String transportRequestNo;

    /**
     * システム配送先番号
     */
    private String systemDeliveryDestinationNo;

    /**
     * 売上確定用送り状番号
     */
    private String salesConfirmationInvoiceNo;

    /**
     * B2お客様管理番号
     */
    private String b2CustomerManagementNumber;

    /**
     * 配送先住所
     */
    @EncryptedField
    private String deliveryAddress;

    /**
     * 配送先メールアドレス
     */
    @EncryptedField
    private String deliveryMailAddress;

    /**
     * 配送先電話番号
     */
    @EncryptedField
    private String deliveryPhoneNumber;

    /**
     * 受注完了時送料計算種別
     */
    private String shipFeeCalcType;

    /**
     * 送料
     */
    private BigDecimal shippingFee;

    /**
     * クロネコゆうメール送料
     */
    private BigDecimal kuronekoYmFee;

    /**
     * クロネコゆうパケット送料
     */
    private BigDecimal kuronekoYpFee;

    /**
     * 宅急便コンパクト送料
     */
    private BigDecimal takkyubinCompactFee;

    /**
     * 宅急便送料
     */
    private BigDecimal takkyubinFee;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * システム顧客番号
     */
    private String sysCustomerId;

    /**
     * 会員ID
     */
    private String memberId;

    /**
     * 伝票番号
     */
    private String deliveryTrackingNo;

    /**
     * 配達希望日
     */
    private String deliveryDate;

    /**
     * 受取時間帯
     */
    private String originalTime;

    /**
     * 送り状発行単位
     */
    private String invoiceUnit;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * メモ
     */
    private String memo;

    /**
     * 配送通知メール送信日時
     */
    private String deliveryNoticeEmailSentDt;

    /**
     * 送り状CSVダウンロード日時
     */
    private String slipCsvDlDt;

    /**
     * 送り状番号取り込み日時
     */
    private String slipNoImportDt;

    /**
     * 原票ナンバー
     */
    private String originalSlipNo;

    /**
     * クール手数料
     */
    private BigDecimal coolDeliveryFee;

    /**
     * 営業所コード
     */
    private String salesOfficeCode;

    /**
     * Version
     */
    private Integer version;

}
