package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssInventorySearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryLocationVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletNumberVo;

import java.util.List;

/**
 * 在庫情報 Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssInventoryService {

    /**
     * パレット番号表の初回印刷に合わせて、在庫情報詳細と在庫履歴を登録する
     *
     * @param pallet パレット情報（積載明細を含む）
     */
    void registerInboundFromPallet(BssPalletNumberVo pallet);

    /**
     * 在庫一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫一覧
     */
    List<BssInventoryListVo> selectStockList(BssInventorySearchBo searchBo);

    /**
     * 品目の在庫詳細を取得する
     *
     * @param itemCode 品目コード
     * @return 在庫詳細。無い場合は null
     */
    BssInventoryItemVo selectItemDetail(String itemCode);

    /**
     * ロケ番の在庫詳細を取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return 在庫詳細。無い場合は null
     */
    BssInventoryLocationVo selectLocationDetail(String whCd, String locCd);
}
