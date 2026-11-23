package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderDetailBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferCandidateVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferDetailLineVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferOpenLineVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 移動依頼 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssTransferOrderMapper {

    /**
     * パレットの移動依頼中明細を取得する
     *
     * @param palletNo パレット番号
     * @param status   移動状況
     * @return 移動依頼明細
     */
    List<BssTransferOpenLineVo> selectOpenByPalletNo(@Param("palletNo") String palletNo,
                                                     @Param("status") String status);

    /**
     * 移動依頼一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動依頼一覧
     */
    List<BssTransferListVo> selectTransferList(BssTransferSearchBo searchBo);

    /**
     * 移動依頼番号の品目を取得する
     *
     * @param requestNos 移動依頼番号
     * @return 品目
     */
    List<BssTransferItemVo> selectItemsByRequestNos(@Param("requestNos") List<String> requestNos);

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
     * @return 登録件数
     */
    int insertTransferOrder(BssTransferOrderBo bo);

    /**
     * 移動依頼明細を登録する
     *
     * @param details 移動依頼明細
     * @return 登録件数
     */
    int insertTransferOrderDetails(@Param("list") List<BssTransferOrderDetailBo> details);

    /**
     * 移動依頼番号のヘッダを取得する
     *
     * @param requestNo 移動依頼番号
     * @return 移動依頼ヘッダ。無い場合は null
     */
    BssTransferDetailVo selectTransferDetailByRequestNo(@Param("requestNo") String requestNo);

    /**
     * 移動依頼番号の明細を取得する
     *
     * @param requestNo 移動依頼番号
     * @return 移動依頼明細
     */
    List<BssTransferDetailLineVo> selectTransferDetailLines(@Param("requestNo") String requestNo);

    /**
     * 移動依頼を更新する
     *
     * @param bo 移動依頼
     * @return 更新件数
     */
    int updateTransferOrder(BssTransferOrderBo bo);

    /**
     * 移動依頼明細の移動数を更新する
     *
     * @param detail 移動依頼明細
     * @return 更新件数
     */
    int updateTransferOrderDetail(BssTransferOrderDetailBo detail);

    /**
     * 移動依頼番号の最大明細番号を取得する
     *
     * @param requestNo 移動依頼番号
     * @return 最大明細番号（明細が無い場合は 0）
     */
    Integer selectMaxDetailNo(@Param("requestNo") String requestNo);

    /**
     * 画面から削除された明細を論理削除する
     *
     * @param requestNo 移動依頼番号
     * @param detailNos 残す明細番号
     * @param updateBy 更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int deleteTransferOrderDetails(@Param("requestNo") String requestNo,
                                   @Param("detailNos") List<Integer> detailNos,
                                   @Param("updateBy") String updateBy,
                                   @Param("updatePgId") String updatePgId);
}
