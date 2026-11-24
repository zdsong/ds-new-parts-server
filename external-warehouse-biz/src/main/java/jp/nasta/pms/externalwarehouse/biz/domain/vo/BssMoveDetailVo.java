package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 移動情報詳細（ヘッダ＋明細）
 */
@Data
public class BssMoveDetailVo {

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 移動状況コード
     */
    private String status;

    /**
     * 移動状況
     */
    private String statusLabel;

    /**
     * 移動元倉庫コード
     */
    private String sourceWhCd;

    /**
     * 移動元
     */
    private String sourceWarehouse;

    /**
     * 移動元ロケコード
     */
    private String sourceLocCd;

    /**
     * 移動先倉庫コード
     */
    private String destWhCd;

    /**
     * 移動先
     */
    private String destWarehouse;

    /**
     * 移動先ロケコード
     */
    private String destLocCd;

    /**
     * 移動元パレット番号
     */
    private String sourcePalletNo;

    /**
     * 移動先パレット番号
     */
    private String destPalletNo;

    /**
     * 移動登録者
     */
    private String registrar;

    /**
     * 移動登録日時
     */
    private String registeredDate;

    /**
     * 移動完了者
     */
    private String completer;

    /**
     * 移動完了日時
     */
    private String completedDate;

    /**
     * 備考
     */
    private String memo;

    /**
     * 移動完了備考
     */
    private String completionMemo;

    /**
     * 明細
     */
    private List<BssMoveDetailLineVo> details;
}
