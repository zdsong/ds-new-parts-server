package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.common.biz.domain.entity.BssInventoryDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssInventoryHistoryEntity;
import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssInventorySearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryHistoryFlatVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryLocationFlatVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryLocationVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryPalletVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletScanLineVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 在庫情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssInventoryMapper {

    /**
     * 在庫情報詳細を登録する
     *
     * @param entity 在庫情報詳細
     * @return 登録件数
     */
    int insertDetail(BssInventoryDetailEntity entity);

    /**
     * 在庫履歴を登録する
     *
     * @param entity 在庫履歴
     * @return 登録件数
     */
    int insertHistory(BssInventoryHistoryEntity entity);

    /**
     * 品目の単位と品目名を取得する
     *
     * @param itemCodes 品目コード
     * @return 品目
     */
    List<MastItemEntity> selectItemsByCodes(@Param("itemCodes") List<String> itemCodes);

    /**
     * ロケーションの保管エリアコードを取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return 保管エリアコード
     */
    String selectZoneCd(@Param("whCd") String whCd, @Param("locCd") String locCd);

    /**
     * 在庫情報詳細をIDで取得する
     *
     * @param id 在庫情報詳細ID
     * @return 在庫情報詳細。無い場合は null
     */
    BssInventoryDetailEntity selectDetailById(@Param("id") String id);

    /**
     * パレット番号の在庫中の明細を取得する（全量移動の判定用）
     *
     * @param palletNo パレット番号
     * @param status   在庫状況コード
     * @return 在庫情報詳細
     */
    List<BssInventoryDetailEntity> selectPaDetailsByPalletNo(@Param("palletNo") String palletNo,
                                                             @Param("status") String status);

    /**
     * 割り当て数量を加算する。バージョン不一致の場合は更新しない
     *
     * @param id         在庫情報詳細ID
     * @param moveQty    移動数量
     * @param version    画面が読み取ったバージョン
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @param lastStatus 最終状態
     * @return 更新件数
     */
    int updateAllocationQty(@Param("id") String id,
                            @Param("moveQty") BigDecimal moveQty,
                            @Param("version") Integer version,
                            @Param("updateBy") String updateBy,
                            @Param("updatePgId") String updatePgId,
                            @Param("lastStatus") String lastStatus);

    /**
     * 移動先の在庫明細の倉庫・ロケ番を更新し、引当数量を 0 にする
     *
     * @param id         在庫情報詳細ID
     * @param whCd       移動先倉庫コード
     * @param locCd      移動先ロケコード
     * @param lastStatus 最終状態
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int updateReceivedInventory(@Param("id") String id,
                                @Param("whCd") String whCd,
                                @Param("locCd") String locCd,
                                @Param("lastStatus") String lastStatus,
                                @Param("updateBy") String updateBy,
                                @Param("updatePgId") String updatePgId);

    /**
     * 移動元の在庫数量と引当数量を移動数だけ減らす
     *
     * @param id         在庫情報詳細ID
     * @param moveQty    移動数量
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int decreaseInventoryForReceived(@Param("id") String id,
                                     @Param("moveQty") BigDecimal moveQty,
                                     @Param("updateBy") String updateBy,
                                     @Param("updatePgId") String updatePgId);

    /**
     * 数量が 0 になった在庫明細を物理削除する
     *
     * @param id 在庫情報詳細ID
     * @return 削除件数
     */
    int deleteInventoryIfEmpty(@Param("id") String id);

    /**
     * 移動完了取消で同一パレットの在庫を移動前の状態に戻す
     *
     * @param id         在庫情報詳細ID
     * @param whCd       移動元倉庫コード
     * @param locCd      移動元ロケコード
     * @param moveQty    移動数量
     * @param lastStatus 最終状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int restoreMatchedInventoryForCancel(@Param("id") String id,
                                         @Param("whCd") String whCd,
                                         @Param("locCd") String locCd,
                                         @Param("moveQty") BigDecimal moveQty,
                                         @Param("lastStatus") String lastStatus,
                                         @Param("updateBy") String updateBy,
                                         @Param("updatePgId") String updatePgId);

    /**
     * 移動完了取消で移動元の数量・引当数量を戻す
     *
     * @param id         在庫情報詳細ID
     * @param moveQty    移動数量
     * @param lastStatus 最終状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int restoreSourceInventoryForCancel(@Param("id") String id,
                                        @Param("moveQty") BigDecimal moveQty,
                                        @Param("lastStatus") String lastStatus,
                                        @Param("updateBy") String updateBy,
                                        @Param("updatePgId") String updatePgId);

    /**
     * 移動完了で登録した移動先在庫を削除する
     *
     * @param palletNo    移動先パレット番号
     * @param itemCode    品目コード
     * @param createPgId  登録プログラムＩＤ
     * @param lastStatus  最終状況
     * @param completedAt 移動完了日時
     * @return 削除件数
     */
    int deleteReceivedInventoryForCancel(@Param("palletNo") String palletNo,
                                         @Param("itemCode") String itemCode,
                                         @Param("createPgId") String createPgId,
                                         @Param("lastStatus") String lastStatus,
                                         @Param("completedAt") String completedAt);

    /**
     * 移動登録削除で在庫の引当数量を戻す
     *
     * @param id         在庫情報詳細ID
     * @param moveQty    移動数量
     * @param lastStatus 最終状況
     * @param updateBy   更新者
     * @param updatePgId 更新プログラムＩＤ
     * @return 更新件数
     */
    int revertAllocationForRegistrationDelete(@Param("id") String id,
                                              @Param("moveQty") BigDecimal moveQty,
                                              @Param("lastStatus") String lastStatus,
                                              @Param("updateBy") String updateBy,
                                              @Param("updatePgId") String updatePgId);

    /**
     * 移動番号に紐づく在庫履歴を物理削除する
     *
     * @param transferNo 移動番号
     * @return 削除件数
     */
    int deleteHistoryByTransferNo(@Param("transferNo") String transferNo);

    /**
     * 移動登録時に登録した在庫履歴（移動予定）を取得する
     *
     * @param transferNo 移動番号
     * @param status     在庫履歴状況コード
     * @return 在庫履歴
     */
    List<BssInventoryHistoryEntity> selectPlannedHistoryByTransferNo(@Param("transferNo") String transferNo,
                                                                     @Param("status") String status);

    /**
     * 移動番号に紐づく移動完了の在庫履歴を物理削除する
     *
     * @param transferNo 移動番号
     * @param status     在庫履歴状況コード
     * @return 削除件数
     */
    int deleteCompletedHistoryByTransferNo(@Param("transferNo") String transferNo,
                                           @Param("status") String status);

    /**
     * パレット番号の在庫明細を取得する
     *
     * @param palletNo パレット番号
     * @return 在庫明細
     */
    List<BssPalletScanLineVo> selectScanLinesByPalletNo(@Param("palletNo") String palletNo);

    /**
     * 在庫一覧を倉庫・ロケ・品目で集約して取得する
     *
     * @param searchBo 検索条件
     * @return 在庫一覧
     */
    List<BssInventoryListVo> selectStockList(BssInventorySearchBo searchBo);

    /**
     * 品目の在庫ヘッダを取得する
     *
     * @param itemCode 品目コード
     * @return 在庫ヘッダ。無い場合は null
     */
    BssInventoryItemVo selectItemHeader(@Param("itemCode") String itemCode);

    /**
     * 品目のパレット一覧を取得する
     *
     * @param itemCode 品目コード
     * @return パレット一覧
     */
    List<BssInventoryPalletVo> selectPalletsByItemCode(@Param("itemCode") String itemCode);

    /**
     * 品目の移動履歴を取得する
     *
     * @param itemCode 品目コード
     * @return 移動履歴
     */
    List<BssInventoryHistoryFlatVo> selectHistoryByItemCode(@Param("itemCode") String itemCode);

    /**
     * ロケ番の在庫ヘッダを取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return 在庫ヘッダ。無い場合は null
     */
    BssInventoryLocationVo selectLocationHeader(@Param("whCd") String whCd, @Param("locCd") String locCd);

    /**
     * ロケ番のパレット品目を取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return パレット品目
     */
    List<BssInventoryLocationFlatVo> selectLocationLines(@Param("whCd") String whCd, @Param("locCd") String locCd);
}
