package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 在庫入庫明細
 *
 * パレット積載明細を、在庫情報詳細と在庫履歴の両方へ登録するときの共通内容。
 *
 * @author nextosd
 */
@Data
public class BssInventoryInboundLineBo {

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 単位
     */
    private String uom;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 保管区分
     */
    private String storageType;

    /**
     * 備考
     */
    private String remarks;
}
