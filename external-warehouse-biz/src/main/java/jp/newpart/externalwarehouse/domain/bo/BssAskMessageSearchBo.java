package jp.newpart.externalwarehouse.biz.domain.bo;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * メッセージ情報 検索パラメータ対象
 *
 * @author nextosd
 */
@Data
public class BssAskMessageSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 注文ID
     */
    private String orderId;
}
