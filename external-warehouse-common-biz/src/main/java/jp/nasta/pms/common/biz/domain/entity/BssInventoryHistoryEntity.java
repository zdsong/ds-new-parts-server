package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 在庫履歴 (bss_inventory_history)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssInventoryHistoryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 在庫履歴情報ID
     */
    private String id;

    /**
     * 保管コード
     */
    private String whCd;

    /**
     * 保管エリアコード
     */
    private String zoneCd;

    /**
     * ロケコード
     */
    private String locCd;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 単位
     */
    private String uom;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * ステータス
     */
    private String status;

    /**
     * 保管区分
     */
    private String storageType;

    /**
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 利用数
     */
    private BigDecimal usedQty;

    /**
     * 紛失/破損数
     */
    private BigDecimal lostDamagedQty;

    /**
     * 移動日時
     */
    private String transferDatetime;

    /**
     * 依頼番号
     */
    private String transferRequestNo;

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 依頼日時
     */
    private String requestDatetime;

    /**
     * 依頼者
     */
    private String requester;

    /**
     * 入出庫日時
     */
    private String ioDatetime;

    /**
     * 入出庫担当者
     */
    private String ioOperator;

    /**
     * 保管期間
     */
    private String storagePeriod;

    /**
     * Version
     */
    private Integer version;
}
