package jp.newpart.externalwarehouse.biz.domain.vo;

import jp.newpart.common.annotation.EncryptedField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 注文情報一覧
 *
 * @author nextosd
 */
@Data
public class BssOrderListVo implements Serializable {
    /**
     * ID
     */
    private String id;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 注文受付日
     */
    private String orderTime;

    /**
     * ステータスラベル
     */
    private String statusLabel;

    /**
     * 商品コード
     */
    private String itemCd;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 依頼主
     */
    @EncryptedField(autoEncrypt = false)
    private String customerName;

    /**
     * 電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String phoneNumber;

    /**
     * 代理注文経路
     */
    private String proxyOrderChLabel;

    /**
     * 注文配送先情報ID
     */
    private String deliveryId;

    /**
     * 届け先
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryCustomerName;

    /**
     * 配送先電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryPhoneNumber;

    /**
     * 届け先（住所）
     */
    @EncryptedField(autoEncrypt = false)
    private String deliveryAddress;

    /**
     * 集荷予定日
     */
    private String deliveryPlanDate;

    /**
     * 決済日
     */
    private String paymentDate;

    /**
     * 出荷日
     */
    private String shipDate;

    /**
     * 配送完了日
     */
    private String deliveryDate;

    /**
     * 伝票番号
     */
    private String deliveryTrackingNo;

    /**
     * 配送方法
     */
    private String deliveryMethod;

    /**
     * 送り状発行単位
     */
    private String invoiceUnit;

    /**
     * 商品情報
     */
    private List<BssOrderItemVo> itemList;
}
