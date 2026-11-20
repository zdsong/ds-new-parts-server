package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ロケ番詳細の品目行
 */
@Data
public class BssInventoryLocationItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String itemCode;

    private String itemName;

    /**
     * 保管区分
     */
    private String storageType;

    /**
     * 保管数
     */
    private BigDecimal quantity;

    /**
     * 使用可否
     */
    private String usable;
}
