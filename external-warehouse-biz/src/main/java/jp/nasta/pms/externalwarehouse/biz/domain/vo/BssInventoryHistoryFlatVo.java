package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 移動履歴の取得行。画面へ返す前にパレット単位へまとめる。
 */
@Data
public class BssInventoryHistoryFlatVo {

    private String id;

    private String palletNo;

    private String storagePeriod;

    private String place;

    private String status;

    private String warehouse;

    private BigDecimal quantity;

    private BigDecimal usedQty;

    private BigDecimal lostQty;

    private String moveAt;

    private String requestNo;

    private String requestAt;

    private String requester;

    private String updatedAt;

    private String updater;
}
