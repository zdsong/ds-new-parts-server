package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 品目マスタ (mast_item)
 *
 * @author nextosd
 */
@Data
public class MastItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 品目名カナ
     */
    private String prprkn;

    /**
     * 品名（伝票用）
     */
    private String prprkj;

    /**
     * 規格
     */
    private String prkku;

    /**
     * 品目区分
     */
    private String prhnmx;

    /**
     * 備考
     */
    private String prbiko;

    /**
     * リビジョン
     */
    private Integer version;
}
