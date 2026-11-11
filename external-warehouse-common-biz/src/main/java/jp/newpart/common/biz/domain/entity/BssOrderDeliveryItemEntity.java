package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 注文配送先商品情報 (bss_order_delivery_item)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssOrderDeliveryItemEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文配送先商品情報ID
     */
    private String id;

    /**
     * 注文番号
     */
    private String orderId;

    /**
     * 配送先ID
     */
    private String deliveryId;

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
     * 金額
     */
    private BigDecimal subtotal;

    /**
     * 金額（税込）
     */
    private BigDecimal subtotalTax;

    /**
     * バリエーション名
     */
    private String variationName;

    /**
     * メモ
     */
    private String memo;

    /**
     * 受取場所名称
     */
    @EncryptedField
    private String recvLocNm;

    /**
     * 受取場所郵便番号
     */
    @EncryptedField
    private String recvPostCd;

    /**
     * 受取場所住所
     */
    @EncryptedField
    private String recvAddr;

    /**
     * 受取場所電話番号
     */
    @EncryptedField
    private String recvTelNo;

    /**
     * 便種ランク
     */
    private String shippingFlg;

    /**
     * 便種
     */
    private String deliveryType;

    /**
     * 商品属性
     */
    private String itemAttr;

    /**
     * 出荷日
     */
    private String shipDate;

    /**
     * ラッピング金額
     */
    private BigDecimal wrapFee;

    /**
     * ラッピング詳細
     */
    private String wrapDetails;

    /**
     * 金額調整タイトル
     */
    private String amtAdjTitle;

    /**
     * 金額調整
     */
    private BigDecimal amtAdjustment;

    /**
     * 税率区分
     */
    private String taxRateCategory;

    /**
     * 税率
     */
    private Integer taxRate;

    /**
     * 値引き小計（標準税率対象）
     */
    private BigDecimal discSubStdTax;

    /**
     * 値引き小計（軽減税率対象）
     */
    private BigDecimal discSubRedTax;

    /**
     * 値引き小計（非課税）
     */
    private BigDecimal discSubTaxExempt;

    /**
     * 合計（標準税率対象）
     */
    private BigDecimal totalStdTax;

    /**
     * 合計内税（標準税率対象）
     */
    private BigDecimal totalIncStdTax;

    /**
     * 合計（軽減税率対象）
     */
    private BigDecimal totalRedTax;

    /**
     * 合計内税（軽減税率対象）
     */
    private BigDecimal totalIncRedTax;

    /**
     * 合計（非課税）
     */
    private BigDecimal totalTaxExempt;

    /**
     * 予備１
     */
    private String rsv1;

    /**
     * 予備２
     */
    private String rsv2;

    /**
     * 予備３
     */
    private String rsv3;

    /**
     * 予備４
     */
    private String rsv4;

    /**
     * 予備５
     */
    private String rsv5;

    /**
     * 予備６
     */
    private String rsv6;

    /**
     * 予備７
     */
    private String rsv7;

    /**
     * 予備８
     */
    private String rsv8;

    /**
     * Version
     */
    private Integer version;

}
