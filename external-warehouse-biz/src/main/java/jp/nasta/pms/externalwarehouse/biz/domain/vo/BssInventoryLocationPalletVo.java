package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * ロケ番にあるパレット
 */
@Data
public class BssInventoryLocationPalletVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String palletNo;

    /**
     * 保管期間
     */
    private String storagePeriod;

    private List<BssInventoryLocationItemVo> items;
}
