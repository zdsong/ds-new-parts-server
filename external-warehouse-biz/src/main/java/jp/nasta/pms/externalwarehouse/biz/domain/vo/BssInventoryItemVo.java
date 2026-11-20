package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 在庫詳細（品目）
 */
@Data
public class BssInventoryItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String itemCode;

    private String itemName;

    /**
     * 製品/部品
     */
    private String classification;

    /**
     * 在庫数
     */
    private BigDecimal quantity;

    /**
     * 最終更新日時
     */
    private String updatedAt;

    /**
     * 最終更新者
     */
    private String updater;

    /**
     * パレット番号一覧
     */
    private List<BssInventoryPalletVo> pallets;

    /**
     * 移動履歴
     */
    private List<BssInventoryHistoryGroupVo> histories;
}
