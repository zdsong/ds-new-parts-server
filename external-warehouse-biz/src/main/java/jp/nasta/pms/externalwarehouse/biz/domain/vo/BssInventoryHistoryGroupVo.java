package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * パレット単位の移動履歴
 */
@Data
public class BssInventoryHistoryGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 保管期間
     */
    private String storagePeriod;

    /**
     * 保管場所
     */
    private String place;

    /**
     * 履歴
     */
    private List<BssInventoryHistoryRowVo> rows;
}
