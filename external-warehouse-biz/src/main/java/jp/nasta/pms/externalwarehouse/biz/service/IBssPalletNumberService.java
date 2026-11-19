package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberEntity;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssPalletNumberBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssPalletNumberSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletNumberVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletScanVo;

import java.util.List;

/**
 * パレット番号 Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssPalletNumberService {

    /**
     * パレット番号を検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return パレット番号リスト（積載明細を含む）
     */
    List<BssPalletNumberVo> selectList(BssPalletNumberSearchBo searchBo);

    /**
     * パレット番号を検索
     *
     * @param palletNo パレット番号
     * @return パレット情報（積載明細を含む）
     */
    BssPalletNumberVo selectByPalletNo(String palletNo);

    /**
     * パレット番号が重複していないか確認する
     *
     * @param palletNo パレット番号
     * @return true:重複なし / false:重複あり
     */
    boolean checkPalletNoUnique(String palletNo);

    /**
     * パレット番号を発番し、パレット情報を登録する
     *
     * 採番したパレット番号は引数の bo に設定して返す。
     *
     * @param bo パレット番号（積載明細を含む）
     * @return 登録件数（ヘッダ）
     */
    int insertPallet(BssPalletNumberBo bo);

    /**
     * パレット情報を更新する（積載明細は入れ替える）
     *
     * @param bo パレット番号（積載明細を含む）
     * @return 更新件数（ヘッダ）
     */
    int updatePallet(BssPalletNumberBo bo);

    /**
     * パレット情報を論理削除する（積載明細は削除する）
     *
     * @param entity パレット番号
     * @return 削除件数（ヘッダ）
     */
    int deletePallet(BssPalletNumberEntity entity);

    /**
     * パレット番号表を印刷済みにする。
     * 未印刷の場合は在庫情報詳細と在庫履歴を登録してから印刷フラグを更新する。
     * 印刷済みの場合は在庫を登録せず、現状のままとする。
     *
     * @param palletNo パレット番号
     * @return 更新件数。存在しない場合は 0
     */
    int updatePrintedFlg(String palletNo);

    /**
     * 移動のバーコード読取結果を組み立てる。
     * パレット番号が無い場合は null。
     *
     * @param palletNo パレット番号
     * @return 読取結果
     */
    BssPalletScanVo selectForMoveScan(String palletNo);
}
