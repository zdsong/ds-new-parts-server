package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 移動一覧に載せる品目
 */
@Data
public class BssMoveItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;
}
