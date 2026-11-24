package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveCompleteBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveRegisterBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveStockSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveRegisterVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveStockVo;
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

    /**
     * 移動登録を行う（移動依頼・在庫引当・在庫履歴をまとめて登録する）
     *
     * @param bo 移動登録の内容
     * @return 移動番号と移動後のパレット番号
     */
    BssMoveRegisterVo registerMove(BssMoveRegisterBo bo);

    /**
     * 移動一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動一覧
     */
    List<BssMoveListVo> selectMoveList(BssMoveSearchBo searchBo);

    /**
     * 移動情報詳細を取得する
     *
     * @param transferNo 移動番号
     * @return 移動情報詳細。無い場合は null
     */
    BssMoveDetailVo selectMoveDetail(String transferNo);

    /**
     * 移動元の在庫明細を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫明細
     */
    List<BssMoveStockVo> selectMoveStockList(BssMoveStockSearchBo searchBo);

    /**
     * パレット番号から移動登録済みの移動情報を取得する
     *
     * @param palletNo パレット番号
     * @return 移動情報。対象が無い場合は null
     */
    BssMoveDetailVo selectReceiveMove(String palletNo);

    /**
     * 移動完了を行う
     *
     * @param bo 移動完了の内容
     */
    void completeMove(BssMoveCompleteBo bo);
}
