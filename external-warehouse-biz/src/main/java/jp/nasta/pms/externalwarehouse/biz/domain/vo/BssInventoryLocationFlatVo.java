package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ロケ番詳細の取得行。画面へ返す前にパレット単位へまとめる。
 */
@Data
public class BssInventoryLocationFlatVo {

    private String id;

    private String warehouse;

    private String location;

    private String updater;

    private String updatedAt;

    private String palletNo;

    private String storagePeriod;

    private String itemCode;

    private String itemName;

    private String storageType;

    private BigDecimal quantity;

    private String usable;
}
