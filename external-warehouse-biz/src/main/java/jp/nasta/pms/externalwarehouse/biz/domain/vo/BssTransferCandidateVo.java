package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動依頼（出庫）品目検索の 1 行（在庫情報詳細）
 */
@Data
public class BssTransferCandidateVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 在庫情報詳細ID
     */
    private String id;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 区分コード
     */
    private String classification;

    /**
     * 区分（製品／部品）
     */
    private String classificationLabel;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 倉庫名
     */
    private String whName;

    /**
     * ロケコード
     */
    private String locCd;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 割り当て数量
     */
    private BigDecimal allocationQty;
}
