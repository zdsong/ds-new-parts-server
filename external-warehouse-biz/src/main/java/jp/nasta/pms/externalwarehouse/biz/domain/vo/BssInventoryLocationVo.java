package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 在庫詳細（ロケ番）
 */
@Data
public class BssInventoryLocationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 保管倉庫
     */
    private String warehouse;

    /**
     * ロケ番
     */
    private String location;

    /**
     * 最終更新者
     */
    private String updater;

    /**
     * 最終更新日時
     */
    private String updatedAt;

    /**
     * パレット番号一覧
     */
    private List<BssInventoryLocationPalletVo> pallets;
}
