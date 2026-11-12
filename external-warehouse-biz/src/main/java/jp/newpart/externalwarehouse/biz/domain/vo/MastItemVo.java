package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品マスタ (mast_item)
 *
 * @author nextosd
 */
@Data
public class MastItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
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
     * 必要収穫量
     */
    private String reqCropYield;

    /**
     * 配送方法
     */
    private String deliveryMethod;

    /**
     * 配送方法
     */
    private String deliveryMethodLabel;


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
     * 便種
     */
    private String shippingFlgLabel;

    /**
     * 宅急便サイズ
     */
    private String courierSizeFlg;

    /**
     * 宅急便サイズ
     */
    private String courierSizeFlgLabel;

    /**
     * 梱包まとめ
     */
    private String ackagingFlg;

    /**
     * 梱包まとめ Label
     */
    private String askagingFlgLabel;

    /**
     * 荷扱い1
     */
    private String handlingType1Flg;

    /**
     * 荷扱い1
     */
    private String handlingType1FlgLabel;

    /**
     * 荷扱い2
     */
    private String handlingType2Flg;

    /**
     * 荷扱い2
     */
    private String handlingType2FlgLabel;

    /**
     * 商品ページ公開フラグ
     */
    private String itemPagePublicFlg;

    /**
     * 販売停止 1:停止 0:販売
     */
    private String salesFlg;

    /**
     * 確認 1:確認 0:確認しない
     */
    private String confirmFlg;

    /**
     * リビジョン
     */
    private Integer version;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 画像
     */
    private List<ImageVo> imageUrls;

    /**
     * 他画像
     */
    private List<ImageVo> imageOtherUrls;

    /**
     * 一覧用画像
     */
    private List<ImageVo> imageListUrls;

    /**
     * og:画像
     */
    private List<ImageVo> imagesOgUrls;
}