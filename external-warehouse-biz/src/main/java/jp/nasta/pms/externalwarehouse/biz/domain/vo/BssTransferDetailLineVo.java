package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動依頼明細の 1 行（詳細画面用）
 */
@Data
public class BssTransferDetailLineVo implements Serializable {

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
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 移動数（使用数）
     */
    private BigDecimal usedQty;

    /**
     * 紛失／破損数
     */
    private BigDecimal lostDamagedQty;

    /**
     * 破棄数
     */
    private BigDecimal disposalQty;

    /**
     * パレット番号
     */
    private String palletNo;
}
