package jp.newpart.externalwarehouse.biz.domain.vo;

import jp.newpart.common.annotation.EncryptedField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 注文配送先情報
 *
 * @author nextosd
 */
@Data
public class BssOrderDeliveryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    private String id;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 届け先
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryCustomerName;

    /**
     * 配送先郵便番号
     */
    @EncryptedField(autoEncrypt = false)
    private String postalCode;

    /**
     * 住所
     */
    @EncryptedField(autoEncrypt = false)
    private String address;

    /**
     * 配送先メールアドレス
     */
    @EncryptedField(autoEncrypt = false)
    private String mailAddress;

    /**
     * 配送先電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String phoneNumber;

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
     * 配送先選択
     */
    private String deliverySelection;

    /**
     * 配送先選択記述内容
     */
    private String deliverySelectionDesc;

    /**
     * 配送方法
     */
    private String deliveryMethod;

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
     * 送料
     */
    private BigDecimal shippingFee;

    /**
     * 荷物ステータス
     */
    private String luggageStatus;

    /**
     * 送料
     */
    private BigDecimal carriage;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * メモ
     */
    private String memo;

    /**
     * 商品合計（税込）
     */
    private BigDecimal itemTotalTax;

    /**
     * 配送先住所
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryAddress;

    /**
     * 配送先メールアドレス
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryMailAddress;

    /**
     * 配送先電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryPhoneNumber;

    /**
     * 商品情報
     */
    private List<BssOrderItemVo> itemList;
}
