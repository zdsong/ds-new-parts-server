package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動依頼中（TF）の明細と注文
 */
@Data
public class BssTransferOpenLineVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 移動依頼番号
     */
    private String transferRequestNo;

    /**
     * 依頼者氏名
     */
    private String requesterName;

    /**
     * 移動希望日
     */
    private String transferShipDate;

    /**
     * 備考
     */
    private String memo;

    /**
     * 移動先倉庫コード
     */
    private String transferToWhCd;

    /**
     * 移動先倉庫名称
     */
    private String toWhNm;
}
