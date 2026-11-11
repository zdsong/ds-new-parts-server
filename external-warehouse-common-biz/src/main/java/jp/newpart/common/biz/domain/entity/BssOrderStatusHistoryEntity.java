package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 注文ステータス履歴情報 (bss_order_status_history)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssOrderStatusHistoryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * ステータス
     */
    private String status;

    /**
     * 対応時間
     */
    private String processDate;

    /**
     * リビジョン
     */
    private Integer version;
}
