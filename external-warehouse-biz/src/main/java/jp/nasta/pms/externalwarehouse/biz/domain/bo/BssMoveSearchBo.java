package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 移動一覧の検索条件
 */
@Data
public class BssMoveSearchBo {

    /**
     * 移動番号（部分一致）
     */
    private String moveNo;

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
     * 品目コード（部分一致）
     */
    private String itemCode;

    /**
     * 品目名（部分一致）
     */
    private String itemName;

    /**
     * 移動登録者（部分一致）
     */
    private String registrar;

    /**
     * 移動完了者（部分一致）
     */
    private String completer;

    /**
     * 移動登録日の開始（yyyyMMdd）
     */
    private String registeredFrom;

    /**
     * 移動登録日の終了（yyyyMMdd）
     */
    private String registeredTo;

    /**
     * 移動完了日の開始（yyyyMMdd）
     */
    private String completedFrom;

    /**
     * 移動完了日の終了（yyyyMMdd）
     */
    private String completedTo;

    /**
     * 移動状況。カンマ区切りで受け取り、検索前に statuses へ分ける
     */
    private String statusCodes;

    /**
     * 移動状況
     */
    private List<String> statuses;
}
