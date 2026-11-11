package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

/**
 * メッセージ情報Vo
 *
 * @author nextosd
 */
@Data
public class BssAskMessageVo {

    /**
     * ID
     */
    private String id;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 農家名
     */
    private String farmerName;

    /**
     * メッセージ内容
     */
    private String messages;

    /**
     * 送信者
     */
    private String sendUser;

    /**
     * 送信時間
     */
    private String sendTime;

    /**
     * 既読ステータス
     */
    private String readStatus;

    /**
     * 既読ステータスラベル
     */
    private String readStatusLabel;

    /**
     * 既読者
     */
    private String readUser;

    /**
     * 受信日時
     */
    private String receptionTime;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * 自分送信：1
     * 他者送信：0
     */
    private String myFlag;
}
