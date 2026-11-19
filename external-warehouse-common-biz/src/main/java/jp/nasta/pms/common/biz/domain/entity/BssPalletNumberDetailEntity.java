package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.nasta.pms.common.annotation.EncryptedField;

/**
 * パレット番号詳細 (bss_pallet_number_detail)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssPalletNumberDetailEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * パレット番号詳細情報ID
     */
    private String id;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 製品群
     */
    private String productGroup;

    /**
     * 手配番号
     */
    private String arrangementNo;

    /**
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 保管容量
     */
    private BigDecimal storageCapacity;

    /**
     * 保管区分
     */
    private String storageType;

    /**
     * 使用可否
     */
    private String usableFlag;

    /**
     * 備考
     */
    private String remarks;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    /**
     * Version
     */
    private Integer version;

}
