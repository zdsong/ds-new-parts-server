package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 移動登録の登録内容（SP402）
 */
@Data
public class BssMoveRegisterBo {

    /**
     * 読み取った移動元パレット番号（全量移動の判定に使う）
     */
    private String palletNo;

    /**
     * 移動依頼の依存があるか
     */
    private boolean dependent;

    /**
     * 移動先倉庫コード
     */
    private String destinationWhCd;

    /**
     * 移動先ロケコード
     */
    private String destinationLocCd;

    /**
     * 備考
     */
    private String remark;

    /**
     * 移動登録者
     */
    private String registrar;

    /**
     * 移動対象の明細
     */
    private List<BssMoveRegisterLineBo> lines;
}
