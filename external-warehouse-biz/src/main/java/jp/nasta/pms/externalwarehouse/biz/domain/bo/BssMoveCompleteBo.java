package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

/**
 * 移動完了の登録内容（SP406）
 */
@Data
public class BssMoveCompleteBo {

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 移動先倉庫コード
     */
    private String destinationWhCd;

    /**
     * 移動先ロケコード
     */
    private String destinationLocCd;

    /**
     * 移動完了備考
     */
    private String completionMemo;
}
