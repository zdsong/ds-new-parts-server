package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferCandidateVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;

import java.util.List;

/**
 * 移動依頼 Serviceインタフェース
 */
public interface IBssTransferOrderService {

    /**
     * 移動依頼一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動依頼一覧
     */
    List<BssTransferListVo> selectTransferList(BssTransferSearchBo searchBo);

    /**
     * 移動依頼（出庫）の対象となる在庫明細を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫明細
     */
    List<BssTransferCandidateVo> selectTransferCandidates(BssTransferCandidateSearchBo searchBo);

    /**
     * 移動依頼を登録する
     *
     * @param bo 移動依頼
     * @return 移動依頼番号
     */
    String insertTransferOrder(BssTransferOrderBo bo);

    /**
     * 移動依頼詳細を取得する
     *
     * @param requestNo 移動依頼番号
     * @return 移動依頼詳細。無い場合は null
     */
    BssTransferDetailVo selectTransferDetail(String requestNo);

    /**
     * 移動依頼を更新する（ヘッダ・明細の移動数）
     *
     * @param bo 移動依頼
     */
    void updateTransferOrder(BssTransferOrderBo bo);
}
