package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 在庫詳細のパレット行
 */
@Data
public class BssInventoryPalletVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 保管場所
     */
    private String warehouse;

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

    /**
     * 備考
     */
    private String remark;
}
