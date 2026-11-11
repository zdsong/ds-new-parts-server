package jp.newpart.externalwarehouse.biz.domain.vo;

import jp.newpart.common.annotation.EncryptedField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 注文情報一覧
 *
 * @author nextosd
 */
@Data
public class BssOrderInfoVo implements Serializable {

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
     * ステータス
     */
    private String statusLabel;

    /**
     * 注文受付日
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
     * 配送方法
     */
    private String deliveryMethod;

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
     * 依頼主
     */
    @EncryptedField(autoEncrypt = false)
    private String customerName;

    /**
     * 依頼主メモ
     */
    private String customerMemo;

    /**
     * 郵便番号
     */
    @EncryptedField(autoEncrypt = false)
    private String postalCode;

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
     * 電話番号
     */
    @EncryptedField(autoEncrypt = false)
    private String phoneNumber;

    /**
     * 商品合計（税込）
     */
    private BigDecimal itemTotalTax;

    /**
     * 商品合計
     */
    private BigDecimal itemTotal;

    /**
     * 合計送料
     */
    private BigDecimal totalCarriage;

    /**
     * お支払い方法
     */
    private String shopPayMethod;

    /**
     * お支払い状況
     */
    private String shopPaySituation;

    /**
     * 受注経路区分
     */
    private String proxyOrderChLabel;

    /**
     * 商品情報
     */
    private List<BssOrderItemVo> itemList;

    /**
     * 届け先情報
     */
    private List<BssOrderDeliveryVo> deliveryList;
}
