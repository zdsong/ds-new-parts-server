package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動履歴の 1 行
 */
@Data
public class BssInventoryHistoryRowVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * ステータス
     */
    private String status;

    /**
     * 保管場所
     */
    private String warehouse;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 利用数
     */
    private BigDecimal usedQty;

    /**
     * 紛失/破損数
     */
    private BigDecimal lostQty;

    /**
     * 移動日時
     */
    private String moveAt;

    /**
     * 移動依頼番号
     */
    private String requestNo;

    /**
     * 移動依頼日時
     */
    private String requestAt;

    /**
     * 移動依頼者
     */
    private String requester;

    /**
     * 更新日時
     */
    private String updatedAt;

    /**
     * 更新者
     */
    private String updater;
}
