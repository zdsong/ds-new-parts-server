package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 注文商品情報
 */
@Data
public class BssOrderItemCustomerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 注文受付日
     */
    private String orderTime;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品
     */
    private String itemName;

    /**
     * 商品コード
     */
    private String itemCode;

    /**
     * システム商品番号
     */
    private String sysItemCode;

    /**
     * 商品カテゴリ(大)
     */
    private String category1;

    /**
     * 商品カテゴリ(中)
     */
    private String category2;

    /**
     * 金額
     */
    private BigDecimal itemPrice;

    /**
     * JANコード
     */
    private String janCode;

    /**
     * スマレジ商品ID
     */
    private String sumarejiItemId;

    /**
     * 必要収穫量
     */

    /**
     * 配送方法
     */
    private String deliveryMethod;
}
