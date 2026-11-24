package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 移動依頼明細の登録内容
 */
@Data
public class BssTransferOrderDetailBo {

    /**
     * 移動依頼明細ID
     */
    private String id;

    /**
     * 移動依頼番号
     */
    private String transferRequestNo;

    /**
     * 明細番号
     */
    private Integer detailNo;

    /**
     * 移動状況
     */
    private String status;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 保管数
     */
    private BigDecimal storageQty;

    /**
     * 使用数
     */
    private BigDecimal usedQty;

    /**
     * 移動済数量
     */
    private BigDecimal movedQty;

    /**
     * 紛失／破損数
     */
    private BigDecimal lostDamagedQty;

    /**
     * 破棄数
     */
    private BigDecimal disposalQty;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 備考
     */
    private String remarks;

    /**
     * 登録者
     */
    private String createBy;

    /**
     * 登録プログラムＩＤ
     */
    private String createPgId;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新プログラムＩＤ
     */
    private String updatePgId;
}
