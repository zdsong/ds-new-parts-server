package jp.newpart.externalwarehouse.biz.domain.bo;

import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.validator.annotation.LmNotBlank;
import jp.newpart.common.validator.utils.ValidatorGroup;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * メッセージ情報 送信
 *
 * @author nextosd
 */
@Data
public class BssAskMessageSendBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 注文ID
     */
    private String orderId;

    /**
     * メッセージ内容
     */
    @LmNotBlank(params = {"{messages}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 512, params = {"{messages}", "512"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String messages;

    /**
     * プログラムＩＤ
     */
    private String pgId;
}
