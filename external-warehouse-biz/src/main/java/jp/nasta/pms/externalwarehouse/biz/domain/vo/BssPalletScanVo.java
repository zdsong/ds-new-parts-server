package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 移動バーコード読取結果
 */
@Data
public class BssPalletScanVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * パレット番号
     */
    private String palletNo;

    /**
     * 品目に移動依頼があるか
     */
    private boolean dependent;

    /**
     * 在庫状況が移動依頼の明細があるか
     */
    private boolean transferRequested;

    /**
     * 画面へ出す注意メッセージ。無い場合は null
     */
    private String warningMessage;

    /**
     * 在庫の品目
     */
    private List<BssPalletScanLineVo> lines;
}
