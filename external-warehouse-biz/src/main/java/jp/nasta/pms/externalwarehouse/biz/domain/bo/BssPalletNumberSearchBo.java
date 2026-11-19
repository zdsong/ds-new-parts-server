package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * パレット番号 検索パラメータ
 *
 * @author nextosd
 */
@Data
public class BssPalletNumberSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * ロケーションコード（ロケ番）
     */
    private String locCd;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;
}
