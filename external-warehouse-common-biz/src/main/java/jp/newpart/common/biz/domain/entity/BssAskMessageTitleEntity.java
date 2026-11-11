package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;

/**
 * メッセージのタイトル (bss_ask_message_title)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssAskMessageTitleEntity extends BaseEntity {

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
     * 送信時間
     */
    private String sendTime;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * メッセージのステータス
     */
    private String status;

    /**
     * Version
     */
    private Integer version;

}
