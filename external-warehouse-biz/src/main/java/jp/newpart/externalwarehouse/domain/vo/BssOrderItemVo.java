package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 注文商品情報
 */
@Data
public class BssOrderItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品
     */
    private String itemId;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 配送先ID
     */
    private String deliveryId;

    /**
     * ステータス
     */
    private String status;

    /**
     * 注文受付日
     */
    private String orderDate;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * システム商品番号
     */
    private String sysItemCode;

    /**
     * 商品コード
     */
    private String itemCd;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品単価
     */
    private BigDecimal itemPrice;

    /**
     * 商品単価（税込）
     */
    private BigDecimal itemPriceTax;

    /**
     * 商品消費税
     */
    private BigDecimal itemSalesTax;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 小計
     */
    private BigDecimal subtotal;

    /**
     * 小計（税込）
     */
    private BigDecimal subtotalTax;

}
