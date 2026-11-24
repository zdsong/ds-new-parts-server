package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 移動読取の在庫品目
 */
@Data
public class BssPalletScanLineVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 区分
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
     * パレット番号
     */
    private String palletNo;

    /**
     * 保管場所
     */
    private String storagePlace;

    /**
     * 在庫状況。画面へは返さない
     */
    @JsonIgnore
    private String status;

    /**
     * 在庫情報詳細のバージョン（移動登録の楽観ロック用）
     */
    private Integer version;

    /**
     * 移動依頼。依頼がない品目は null
     */
    private BssPalletScanRequestVo request;
}
