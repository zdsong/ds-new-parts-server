package jp.newpart.externalwarehouse.biz.domain.bo;

import jp.newpart.common.biz.domain.bo.DateCommonSearchBo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 注文一覧 検索パラメータ対象
 *
 * @author nextosd
 */
@Data
public class BssOrderSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 注文日 開始 - 終了
     */
    private DateCommonSearchBo orderDate;

    /**
     * 集荷予定日 開始 - 終了
     */
    private DateCommonSearchBo deliveryPlanDate;

    /**
     * ステータス
     */
    private String status;

    /**
     * 伝票番号
     */
    private String deliveryTrackingNo;

    /**
     * 商品コード
     */
    private String itemCd;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 依頼主
     */
    private String customerName;

    /**
     * 電話番号
     */
    private String phoneNumber;

    /**
     * 届け先
     */
    private String deliveryCustomerName;

    /**
     * 配送先電話番号
     */
    private String deliveryPhoneNumber;

    /**
     * PCクエリ
     */
    private Boolean pcSearchFlg;

    /**
     * 代理注文経路リスト
     */
    private List<String> proxyOrderChList;

}
