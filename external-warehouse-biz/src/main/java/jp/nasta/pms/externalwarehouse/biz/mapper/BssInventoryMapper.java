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
