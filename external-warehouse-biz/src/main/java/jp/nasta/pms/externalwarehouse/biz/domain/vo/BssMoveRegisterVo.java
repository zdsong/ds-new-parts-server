package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 移動登録の結果（SP402）
 */
@Data
public class BssMoveRegisterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 移動後のパレット番号
     */
    private String palletNo;

    /**
     * 登録方式。whole:パレットをそのまま移動 / issue:新しいパレットを発番
     */
    private String mode;
}
