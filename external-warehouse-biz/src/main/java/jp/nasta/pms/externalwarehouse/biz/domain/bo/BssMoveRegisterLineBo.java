package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 移動登録の明細（SP402）
 */
@Data
public class BssMoveRegisterLineBo {

    /**
     * 在庫情報詳細ID
     */
    private String id;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 区分（1:製品 / 3:部品）
     */
    private String category;

    /**
     * 移動数量
     */
    private BigDecimal moveQty;

    /**
     * 保管数量
     */
    private BigDecimal storageQty;

    /**
     * 移動元のパレット番号
     */
    private String palletNo;

    /**
     * 在庫情報詳細の楽観ロック用バージョン
     */
    private Integer version;

    /**
     * 移動依頼番号（依存がある場合のみ）
     */
    private String requestNo;

    /**
     * 移動依頼明細番号（依存がある場合のみ）
     */
    private Integer requestDetailNo;
}
