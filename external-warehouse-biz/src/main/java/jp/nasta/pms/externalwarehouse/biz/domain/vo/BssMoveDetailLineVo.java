package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動情報詳細の明細 1 行
 */
@Data
public class BssMoveDetailLineVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 明細番号
     */
    private Integer detailNo;

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
     * 品目区分コード
     */
    private String classificationCode;

    /**
     * 在庫情報詳細ID
     */
    private String inventoryDetailId;

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
     * 移動依頼番号
     */
    private String transferRequestNo;

    /**
     * 移動依頼明細番号
     */
    private Integer transferRequestDetailNo;

    /**
     * 備考
     */
    private String remarks;
}
