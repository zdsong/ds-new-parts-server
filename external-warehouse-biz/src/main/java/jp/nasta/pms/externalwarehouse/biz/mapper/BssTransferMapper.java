package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.common.biz.domain.entity.BssTransferDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssTransferEntity;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveStockSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderDetailBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveDetailLineVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveStockVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 移動管理 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssTransferMapper {

    /**
     * 移動管理を登録する
     *
     * @param entity 移動管理
     * @return 登録件数
     */
    int insertTransfer(BssTransferEntity entity);

    /**
     * 移動管理詳細を登録する
     *
     * @param details 移動管理詳細
     * @return 登録件数
     */
    int insertTransferDetails(@Param("list") List<BssTransferDetailEntity> details);

    /**
     * 移動一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動一覧
     */
    List<BssMoveListVo> selectMoveList(BssMoveSearchBo searchBo);

    /**
     * 移動番号の品目を取得する
     *
     * @param transferNos 移動番号
     * @return 品目
     */
    List<BssMoveItemVo> selectMoveItemsByTransferNos(@Param("transferNos") List<String> transferNos);

    /**
     * 移動番号のヘッダを取得する
     *
     * @param transferNo 移動番号
     * @return 移動情報。無い場合は null
     */
    BssMoveDetailVo selectMoveDetail(@Param("transferNo") String transferNo);

    /**
     * パレット番号から移動登録済みの移動番号を取得する
     *
     * @param palletNo パレット番号
     * @return 移動番号。対象が無い場合は null
     */
    String selectPendingTransferNoByPalletNo(@Param("palletNo") String palletNo,
                                              @Param("status") String status);

    /**
     * 移動管理を移動完了にする
     *
     * @param transferNo     移動番号
     * @param completionMemo 移動完了備考
     * @param completedBy    移動完了者
     * @param completedAt    移動完了日時
     * @param updateBy       更新者
     * @param updatePgId     更新プログラムＩＤ
     * @return 更新件数
     */
    int updateTransferCompleted(@Param("transferNo") String transferNo,
                                @Param("status") String status,
                                @Param("fromStatus") String fromStatus,
                                @Param("completionMemo") String completionMemo,
                                @Param("completedBy") String completedBy,
                                @Param("completedAt") String completedAt,
                                @Param("updateBy") String updateBy,
                                @Param("updatePgId") String updatePgId);

    /**
     * 移動管理詳細の状況を更新する
     *
     * @param transferNo 移動番号
     * @param status     移動状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int updateTransferDetailStatus(@Param("transferNo") String transferNo,
                                   @Param("status") String status,
                                   @Param("updateBy") String updateBy,
                                   @Param("updatePgId") String updatePgId);

    /**
     * 移動番号の明細を取得する
     *
     * @param transferNo 移動番号
     * @return 移動明細
     */
    List<BssMoveDetailLineVo> selectMoveDetailLines(@Param("transferNo") String transferNo);

    /**
     * 移動完了を取り消して移動登録済の状態に戻す
     *
     * @param transferNo 移動番号
     * @param status     取消後の状況
     * @param fromStatus 取消前の状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int cancelTransfer(@Param("transferNo") String transferNo,
                       @Param("status") String status,
                       @Param("fromStatus") String fromStatus,
                       @Param("updateBy") String updateBy,
                       @Param("updatePgId") String updatePgId);

    /**
     * 移動登録時の移動元ロケ番を在庫履歴から取得する
     *
     * @param transferNo 移動番号
     * @param itemCode   品目コード
     * @param palletNo   移動先パレット番号
     * @return 移動元ロケ番
     */
    String selectMoveSourceLocCd(@Param("transferNo") String transferNo,
                                 @Param("itemCode") String itemCode,
                                 @Param("palletNo") String palletNo);

    /**
     * 移動管理を排他ロックして取得する
     *
     * @param transferNo 移動番号
     * @return 移動管理
     */
    BssTransferEntity selectTransferForUpdate(@Param("transferNo") String transferNo);

    /**
     * 移動管理（明細）を物理削除する
     *
     * @param transferNo 移動番号
     * @return 削除件数
     */
    int deleteTransferDetailsPhysically(@Param("transferNo") String transferNo);

    /**
     * 移動管理（ヘッダ）を物理削除する
     *
     * @param transferNo 移動番号
     * @return 削除件数
     */
    int deleteTransferPhysically(@Param("transferNo") String transferNo);

    /**
     * 移動依頼明細の移動済数量を減算する
     *
     * @param requestNo 移動依頼番号
     * @param detailNo  明細番号
     * @param moveQty   移動数量
     * @param status    減算後の明細状況
     * @param updateBy  更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int subtractRequestDetailMovedQty(@Param("requestNo") String requestNo,
                                      @Param("detailNo") Integer detailNo,
                                      @Param("moveQty") BigDecimal moveQty,
                                      @Param("status") String status,
                                      @Param("updateBy") String updateBy,
                                      @Param("updatePgId") String updatePgId);

    /**
     * 移動登録削除後、未完了の移動依頼を移動依頼に戻す
     *
     * @param requestNo 移動依頼番号
     * @param status    戻す状況
     * @param updateBy  更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int revertRequestStatus(@Param("requestNo") String requestNo,
                            @Param("status") String status,
                            @Param("updateBy") String updateBy,
                            @Param("updatePgId") String updatePgId);

    /**
     * 移動元の在庫明細を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫明細
     */
    List<BssMoveStockVo> selectMoveStockList(BssMoveStockSearchBo searchBo);

    /**
     * 移動登録の対象になる移動依頼明細を取得する
     *
     * @param requestNo 移動依頼番号
     * @param detailNo  移動依頼明細番号
     * @return 移動依頼明細。無い場合は null
     */
    BssTransferOrderDetailBo selectRequestDetailForMove(@Param("requestNo") String requestNo,
                                                        @Param("detailNo") Integer detailNo);

    /**
     * 移動依頼明細の移動済数量を加算する
     *
     * @param requestNo  移動依頼番号
     * @param detailNo   移動依頼明細番号
     * @param moveQty    移動数
     * @param status     移動依頼明細状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int addRequestDetailMovedQty(@Param("requestNo") String requestNo,
                                 @Param("detailNo") Integer detailNo,
                                 @Param("moveQty") BigDecimal moveQty,
                                 @Param("status") String status,
                                 @Param("updateBy") String updateBy,
                                 @Param("updatePgId") String updatePgId);

    /**
     * 全明細の移動が完了した移動依頼の状況を移動登録完了にする
     *
     * @param requestNo  移動依頼番号
     * @param status     移動状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int updateRequestStatusWhenCompleted(@Param("requestNo") String requestNo,
                                         @Param("status") String status,
                                         @Param("updateBy") String updateBy,
                                         @Param("updatePgId") String updatePgId);
}
