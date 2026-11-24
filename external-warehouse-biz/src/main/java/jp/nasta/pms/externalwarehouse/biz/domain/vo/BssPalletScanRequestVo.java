package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 移動読取の品目に紐づく移動依頼
 */
@Data
public class BssPalletScanRequestVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 移動依頼番号
     */
    private String requestNo;

    /**
     * 移動依頼明細番号
     */
    private Integer requestDetailNo;

    /**
     * 移動先
     */
    private String destinationLabel;

    /**
     * 依頼者
     */
    private String requester;

    /**
     * 移動希望日
     */
    private String desiredDate;

    /**
     * 備考
     */
    private String remark;
}
