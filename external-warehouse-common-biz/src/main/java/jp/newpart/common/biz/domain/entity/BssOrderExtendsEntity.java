package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 注文拡情報 (bss_order_extends)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssOrderExtendsEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文番号
     */
    private String orderId;

    /**
     * 決済手数料
     */
    private String paymentFee;

    /**
     * 注文確認メール送信日時
     */
    private String orderConfirmEmailSentDt;

    /**
     * 売上確定額
     */
    private BigDecimal confirmedSalesAmt;

    /**
     * 売上確定日
     */
    private String salesConfirmDate;

    /**
     * ポイント利用額
     */
    private Integer pointsUsedAmt;

    /**
     * お支払い方法
     */
    private String shopPayMethod;

    /**
     * お支払い方法補足
     */
    private String shopPayMethodSuppItems;

    /**
     * アンケート
     */
    private String questionnaire;

    /**
     * 利用ポイント
     */
    private String usagePoints;

    /**
     * 獲得ポイント
     */
    private String earnedPoints;

    /**
     * 売上確定登録結果
     */
    private String salesConfirmRegResult;

    /**
     * 会員ランク
     */
    private String memberFlg;

    /**
     * 会員ランク特典
     */
    private String memberRankBenefit;

    /**
     * 会員ランク値引き
     */
    private String memberRankDiscount;

    /**
     * 定期購入番号
     */
    private String subOrderNo;

    /**
     * 受注確定回数
     */
    private Integer confOrderCnt;

    /**
     * 定期購入全回数
     */
    private Integer totalSubCnt;

    /**
     * 認証番号
     */
    private String authNo;

    /**
     * 独自ステータス
     */
    private String customStatus;

    /**
     * 代理注文経路
     */
    private String proxyOrderCh;

    /**
     * 代理注文経路説明内容
     */
    private String proxyOrderChDesc;

    /**
     * 代理注文登録者
     */
    private String proxyOrderReg;

    /**
     * ご依頼主
     */
    private String reqBy;

    /**
     * 予約注文
     */
    private String reservedOrder;

    /**
     * クーポン情報
     */
    private String couponInfo;

    /**
     * クーポン値引き
     */
    private String couponDisc;

    /**
     * 販促メール配信
     */
    private String promoEmail;

    /**
     * QRURLパラメータ
     */
    private String qrUrlParam;

    /**
     * ステータス説明内容
     */
    private String statusDesc;

    /**
     * Version
     */
    private Integer version;

}
