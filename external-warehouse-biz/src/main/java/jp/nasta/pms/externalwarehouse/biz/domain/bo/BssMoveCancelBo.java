package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

/**
 * 移動完了取消の内容（P407）
 */
@Data
public class BssMoveCancelBo {

    /**
     * 移動番号
     */
    private String transferNo;
}
