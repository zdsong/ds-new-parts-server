package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 移動依頼詳細（ヘッダ＋明細）
 */
@Data
public class BssTransferDetailVo {

    /**
     * 移動依頼番号
     */
    private String requestNo;

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
     * 移動先倉庫コード
     */
    private String destWhCd;

    /**
     * 移動先
     */
    private String destWarehouse;

    /**
     * 移動依頼日
     */
    private String requestDate;

    /**
     * 移動希望日
     */
    private String desiredDate;

    /**
     * 依頼者氏名
     */
    private String requester;

    /**
     * 備考
     */
    private String memo;

    /**
     * 明細
     */
    private List<BssTransferDetailLineVo> details;
}
