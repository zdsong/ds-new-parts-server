package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 移動依頼（出庫）品目検索の検索条件
 */
@Data
public class BssTransferCandidateSearchBo {

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 区分。カンマ区切りで受け取り、検索前に classifications へ分ける
     */
    private String classificationCodes;

    /**
     * 区分
     */
    private List<String> classifications;

    /**
     * 品目コード（部分一致）
     */
    private String itemCode;

    /**
     * 品目名（部分一致）
     */
    private String itemName;

    /**
     * 在庫状況
     */
    private String status;
}
