package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 商品マスタ (mast_item)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastItemEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品マスタ情報ID
     */
    private String id;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品コード
     */
    private String itemCode;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * JANコード
     */
    private String janCode;

    /**
     * スマレジ商品ID
     */
    private String sumarejiItemId;

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
     * 販売価格
     */
    private BigDecimal salesPrice;

    /**
     * 通常価格
     */
    private BigDecimal normalPrice;

    /**
     * 会員価格
     */
    private BigDecimal memberPrice;

    /**
     * 便種
     */
    private String shippingFlg;

    /**
     * 宅急便サイズ
     */
    private String courierSizeFlg;

    /**
     * 梱包まとめ
     */
    private String ackagingFlg;

    /**
     * 荷扱い1
     */
    private String handlingType1Flg;

    /**
     * 荷扱い2
     */
    private String handlingType2Flg;

    /**
     * 商品ページ公開フラグ
     */
    private String itemPagePublicFlg;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 販売停止有無
     */
    private String salesFlg;

    /**
     * 確認有無
     */
    private String confirmFlg;

    /**
     * 有効無効フラグ
     */
    private String invalidValidFlg;

    /**
     * Version
     */
    private Integer version;

}
