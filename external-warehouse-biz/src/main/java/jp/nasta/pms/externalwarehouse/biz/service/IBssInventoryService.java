package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletNumberVo;

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
}
