package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 在庫一覧の 1 行（倉庫・ロケ・品目で集約）
 */
@Data
public class BssInventoryListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行の識別子
     */
    private String id;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 保管倉庫
     */
    private String warehouse;

    /**
     * ロケ番
     */
    private String location;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 在庫数
     */
    private BigDecimal quantity;

    /**
     * 最終更新日
     */
    private String updatedDate;

    /**
     * 最終更新者
     */
    private String updater;
}
