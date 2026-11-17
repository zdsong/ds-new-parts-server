package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.biz.domain.entity.MastItemEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品目マスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastItemMapper {

    /**
     * 品目マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 品目マスタリスト
     */
    List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo);

    /**
     * 品目コードで品目マスタを検索
     *
     * @param itemCode 品目コード
     * @return 品目マスタ
     */
    MastItemVo selectByItemCode(@Param("itemCode") String itemCode);

    /**
     * 品目マスタを登録
     *
     * @param entity 品目マスタ
     * @return 登録件数
     */
    int insert(MastItemEntity entity);

    /**
     * 品目マスタを更新
     *
     * @param entity 品目マスタ
     * @return 更新件数
     */
    int update(MastItemEntity entity);
}
