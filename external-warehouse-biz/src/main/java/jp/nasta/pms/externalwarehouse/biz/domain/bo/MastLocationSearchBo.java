package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ロケーションマスタ 検索パラメータ
 *
 * @author nextosd
 */
@Data
public class MastLocationSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * ロケーションコード
     */
    private String locCd;
}
