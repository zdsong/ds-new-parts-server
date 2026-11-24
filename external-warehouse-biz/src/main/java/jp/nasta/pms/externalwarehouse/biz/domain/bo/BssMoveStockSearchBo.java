package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

/**
 * 移動元在庫検索の検索条件
 */
@Data
public class BssMoveStockSearchBo {

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 品目コード（部分一致）
     */
    private String itemCode;

    /**
     * 品目名（部分一致）
     */
    private String itemName;

    /**
     * 在庫状況
     */
    private String status;
}
