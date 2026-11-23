package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 移動依頼の登録内容
 */
@Data
public class BssTransferOrderBo {

    /**
     * 移動依頼ID
     */
    private String id;

    /**
     * 移動依頼番号
     */
    private String requestNo;

    /**
     * 移動状況
     */
    private String status;

    /**
     * 移動元倉庫コード
     */
    private String sourceWhCd;

    /**
     * 移動先倉庫コード
     */
    private String destWhCd;

    /**
     * 移動先ロケコード
     */
    private String destLocCd;

    /**
     * 移動依頼日（yyyy/MM/dd）
     */
    private String transferDate;

    /**
     * 移動希望日（yyyy/MM/dd）
     */
    private String desiredDate;

    /**
     * 依頼者氏名
     */
    private String requester;

    /**
     * 登録者
     */
    private String registeredBy;

    /**
     * 登録日（yyyy/MM/dd）
     */
    private String registrationDate;

    /**
     * 備考
     */
    private String memo;

    /**
     * 登録プログラムＩＤ
     */
    private String createPgId;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新プログラムＩＤ
     */
    private String updatePgId;

    /**
     * 移動依頼明細
     */
    private List<BssTransferOrderDetailBo> details;
}
