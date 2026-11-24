package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 移動管理詳細 (bss_transfer_detail)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssTransferDetailEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 移動管理詳細情報ID
     */
    private String id;

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 明細番号
     */
    private Integer detailNo;

    /**
     * 移動依頼番号
     */
    private String transferRequestNo;

    /**
     * 移動依頼明細番号
     */
    private Integer transferRequestDetailNo;

    /**
     * 在庫情報詳細ID
     */
    private String inventoryDetailId;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 品目区分
     */
    private String classification;

    /**
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 移動数
     */
    private BigDecimal moveQty;

    /**
     * 紛失／破損数
     */
    private BigDecimal lostDamagedQty;

    /**
     * 破棄数
     */
    private BigDecimal disposalQty;

    /**
     * 移動元パレット番号
     */
    private String sourcePalletNo;

    /**
     * 移動先パレット番号
     */
    private String destPalletNo;

    /**
     * パレット新規発番フラグ
     */
    private String palletIssueFlg;

    /**
     * 移動状況
     */
    private String status;

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
