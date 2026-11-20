package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

/**
 * 在庫一覧の検索条件
 */
@Data
public class BssInventorySearchBo {

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * ロケコード
     */
    private String locCd;

    /**
     * 品目コード（部分一致）
     */
    private String itemCode;

    /**
     * 品目名（部分一致）
     */
    private String itemName;
}
