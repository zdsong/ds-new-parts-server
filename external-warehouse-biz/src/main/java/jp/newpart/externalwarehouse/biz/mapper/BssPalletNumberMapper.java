package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.newpart.common.biz.domain.entity.BssPalletNumberEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.BssPalletNumberSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssPalletNumberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * パレット番号 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssPalletNumberMapper {

    /**
     * パレット番号を検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return パレット番号リスト
     */
    List<BssPalletNumberVo> selectList(BssPalletNumberSearchBo searchBo);

    /**
     * パレット番号を検索
     *
     * @param palletNo パレット番号
     * @return パレット情報
     */
    BssPalletNumberVo selectByPalletNo(@Param("palletNo") String palletNo);

    /**
     * 積載明細を検索する
     *
     * @param palletNos パレット番号リスト
     * @return 積載明細リスト
     */
    List<BssPalletNumberDetailEntity> selectDetailsByPalletNos(@Param("palletNos") List<String> palletNos);

    /**
     * パレット番号が存在するか確認する
     *
     * @param palletNo パレット番号
     * @return 件数
     */
    int countByPalletNo(@Param("palletNo") String palletNo);

    /**
     * パレット番号を登録
     *
     * @param entity パレット番号
     * @return 登録件数
     */
    int insert(BssPalletNumberEntity entity);

    /**
     * パレット番号を更新（楽観的排他制御）
     *
     * @param entity パレット番号
     * @return 更新件数
     */
    int update(BssPalletNumberEntity entity);

    /**
     * パレット番号を論理削除
     *
     * @param entity パレット番号
     * @return 削除件数
     */
    int deleteLogical(BssPalletNumberEntity entity);

    /**
     * 印刷フラグを印刷済み（1）に更新
     *
     * @param entity パレット番号（palletNo と更新項目を設定する）
     * @return 更新件数
     */
    int updatePrintedFlg(BssPalletNumberEntity entity);

    /**
     * 積載明細を登録
     *
     * @param entity パレット番号詳細
     * @return 登録件数
     */
    int insertDetail(BssPalletNumberDetailEntity entity);

    /**
     * 積載明細を論理削除（パレット番号単位）
     *
     * @param entity パレット番号詳細（palletNo と更新項目を設定する）
     * @return 削除件数
     */
    int deleteDetailsByPalletNo(BssPalletNumberDetailEntity entity);
}
