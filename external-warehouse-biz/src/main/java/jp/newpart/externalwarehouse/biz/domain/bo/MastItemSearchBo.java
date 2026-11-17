package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 品目マスタ 検索パラメータ対象
 *
 * @author nextosd
 */
@Data
public class MastItemSearchBo implements Serializable {

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
     * 品目区分
     */
    private List<String> prhnmx;
}
