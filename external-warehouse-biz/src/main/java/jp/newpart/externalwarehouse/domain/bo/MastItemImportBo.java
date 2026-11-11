package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品マスタ インポート CSV Data
 */
@Data
public class MastItemImportBo {
    /**
     * システム商品番号
     */
    private String sysItemCode;

    /**
     * 有効無効フラグ
     */
    private String invalidValidFlg;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品コード
     */
    private String itemCode;

    /**
     * JANコード
     */
    private String janCode;

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
     * 梱包まとめ
     */
    private String ackagingFlg;

    /**
     * 商品ページ公開フラグ
     */
    private String itemPagePublicFlg;

    /**
     * 宅急便サイズ
     */
    private String courierSizeFlg;

    /**
     * 荷扱い1
     */
    private String handlingType1Flg;

    /**
     * 荷扱い1ラベル
     */
    private String handlingType1Label;

    /**
     * 荷扱い2
     */
    private String handlingType2Flg;

    /**
     * スマレジ商品ID
     */
    private String sumarejiItemId;

    /**
     * バリエーション名
     */
    private String variationName;

    /**
     * 在庫管理フラグ
     */
    private String invMgmtFlg;

    /**
     * 残在庫
     */
    private String remStock;

    /**
     * 更新後在庫
     */
    private String stockAfterUpdate;

    /**
     * 最低基準在庫
     */
    private String minBenchStock;

    /**
     * カテゴリ番号
     */
    private String categoryNo;

    /**
     * キャンペーン番号
     */
    private String campNo;

    /**
     * 消費税
     */
    private String tax;

    /**
     * メーカー
     */
    private String manufacturerName;

    /**
     * ブランド
     */
    private String brandName;

    /**
     * 従量ポイント
     */
    private String volPoints;

    /**
     * 個別送料
     */
    private String indShipFee;

    /**
     * 配送方法除外番号
     */
    private String exclShipMethodNo;

    /**
     * お支払い方法除外番号
     */
    private String exclPayMethodNo;

    /**
     * 個別付与ポイント
     */
    private String indGrantedPoints;

    /**
     * 同時表示グループ番号
     */
    private String simDispGroupNo;

    /**
     * 注文年齢制限（下限）
     */
    private String orderAgeMin;

    /**
     * 注文年齢制限（上限）
     */
    private String orderAgeMax;

    /**
     * 注文数制限（下限）
     */
    private String orderQtyMin;

    /**
     * 注文数制限（上限）
     */
    private String orderQtyMax;

    /**
     * 販売期間（下限）
     */
    private String salesMinPeriod;

    /**
     * 販売期間（上限）
     */
    private String salesMaxPeriod;

    /**
     * ロケーション
     */
    private String location;

    /**
     * 商品バッジ番号
     */
    private String prodBadgeNo;

    /**
     * 数量入力フォーム
     */
    private String qtyInputForm;

    /**
     * 数値入力選択肢
     */
    private String numInputOpt;

    /**
     * バリエーションパターン１表示
     */
    private String varPattern1Disp;

    /**
     * 商品情報１
     */
    private String itemInfo1;

    /**
     * 商品情報２
     */
    private String itemInfo2;

    /**
     * 商品情報３
     */
    private String itemInfo3;

    /**
     * title
     */
    private String title;

    /**
     * meta-description
     */
    private String metaDescription;

    /**
     * og:title
     */
    private String ogTitle;

    /**
     * og:type
     */
    private String ogType;

    /**
     * og:imageURL
     */
    private String ogImageUrl;

    /**
     * 新着商品掲載フラグ
     */
    private String newProdListFlg;

    /**
     * バリエーションパターン
     */
    private String variationPattern;

    /**
     * バリエーションパターン２項目
     */
    private String variationPattern2Items;

    /**
     * ロッカー受け取りサイズ
     */
    private String lockerPickupSize;

    /**
     * 予約お届け期間番号
     */
    private String resvAcceptPeriodNo;

    /**
     * クーポン利用除外フラグ
     */
    private String couponExclFlg;

    /**
     * 税率区分
     */
    private String taxRateCategory;

    /**
     * 販売時間指定
     */
    private String salesTimeSpec;

    /**
     * 販売時間（下限）
     */
    private String lowerLimitSalesHours;

    /**
     * 販売時間（上限）
     */
    private String upperLimitSalesHours;

    /**
     * 発荷主コード
     */
    private String shipperCode;

    /**
     * 優先順位
     */
    private String priority;

    /**
     * 返品区分番号
     */
    private String returnClassificationNo;

    /**
     * 注文不可指定
     */
    private String orderProhibitSpecFlg;

    /**
     * 定期購入商品設定
     */
    private String subProdSetting;

    /**
     * 定期購入価格（初回価格）
     */
    private BigDecimal subPriceFirst;

    /**
     * 定期購入価格（2回目以降価格）
     */
    private BigDecimal subPriceSecond;

    /**
     * 定期購入回数
     */
    private Integer subCnt;

    /**
     * 定期購入専用フラグ
     */
    private String subOnlyFlg;

    /**
     * 定期購入商品説明
     */
    private String subProdDesc;

    /**
     * お気に入りリスト表示フラグ
     */
    private String favListDispFlg;

    /**
     * 入荷通知メールフラグ
     */
    private String restockNotifyFlg;

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
     * 予備９
     */
    private String rsv9;

    /**
     * 画像URL
     */
    private String imageUrl;

    /**
     * 他画像URL
     */
    private String imageOtherUrl;

    /**
     * 一覧用表示画像URL
     */
    private String imageListUrl;
}
