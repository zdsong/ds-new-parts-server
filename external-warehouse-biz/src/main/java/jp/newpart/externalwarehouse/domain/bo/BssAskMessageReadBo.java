package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * メッセージ情報 既読
 *
 * @author nextosd
 */
@Data
public class BssAskMessageReadBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * プログラムＩＤ
     */
    private String pgId;
}
