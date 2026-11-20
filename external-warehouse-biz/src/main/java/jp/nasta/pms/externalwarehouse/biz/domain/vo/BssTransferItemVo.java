package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

/**
 * 移動依頼の品目
 */
@Data
public class BssTransferItemVo {

    /**
     * 移動依頼番号
     */
    private String requestNo;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;
}
