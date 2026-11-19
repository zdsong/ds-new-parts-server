package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 在庫情報詳細 (bss_inventory_detail)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssInventoryDetailEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 在庫情報詳細情報ID
     */
    private String id;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * バーコード
     */
    private String barcode;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * ロケコード
     */
    private String locCd;

    /**
     * 区分
     */
    private String classification;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 割り当て数量
     */
    private BigDecimal allocationQty;

    /**
     * 在庫状況
     */
    private String status;

    /**
     * 最後在庫状況
     */
    private String lastStatus;

    /**
     * 備考
     */
    private String remarks;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    /**
     * Version
     */
    private Integer version;
}
