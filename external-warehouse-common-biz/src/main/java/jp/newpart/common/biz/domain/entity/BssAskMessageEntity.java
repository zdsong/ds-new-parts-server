package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * メッセージ (bss_ask_message)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssAskMessageEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * メッセージID
     */
    private String id;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * メッセージ内容
     */
    private String messages;

    /**
     * 送信者
     */
    private String sendUser;

    /**
     * 送信日時
     */
    private String sendTime;

    /**
     * 既読ステータス
     */
    private String readStatus;

    /**
     * 既読者
     */
    private String readUser;

    /**
     * 受信日時
     */
    private String receptionTime;

    /**
     * メッセージの種類
     */
    private String messageType;

    /**
     * システムメッセージフラグ
     */
    private String sysMessageFlg;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * Version
     */
    private Integer version;

}
