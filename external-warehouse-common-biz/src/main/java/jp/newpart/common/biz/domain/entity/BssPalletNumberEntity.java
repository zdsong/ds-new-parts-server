package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * パレット番号 (bss_pallet_number)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssPalletNumberEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * パレット番号情報ID
     */
    private String id;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * バーコード
     */
    private String barcode;

    /**
     * 保管期間
     */
    private String storagePeriod;

    /**
     * 部品区分
     */
    private String classification;

    /**
     * 入庫日
     */
    private String inboundDate;

    /**
     * 保管場所
     */
    private String whCd;

    /**
     * 保管エリアコード
     */
    private String zoneCd;

    /**
     * ロケ番
     */
    private String locCd;

    /**
     * パレット番号合并フラグ
     */
    private String mergeFlg;

    /**
     * 印刷フラグ
     */
    private String printedFlg;

    /**
     * 備考1
     */
    private String memo1;

    /**
     * 備考2
     */
    private String memo2;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    /**
     * Version
     */
    private Integer version;

}
