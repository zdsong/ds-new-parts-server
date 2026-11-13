package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.externalwarehouse.biz.domain.vo.MastItemImageVo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品マスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastItemMapper {

    /**
     * 商品マスタを検索
     *
     * @param id 商品マスタプライマリ・キー
     * @return 商品マスタ
     */
     MastItemVo selectById(@Param("id") String id);

    /**
     * 商品マスタを検索
     *
     * @param itemId 商品Id
     * @return 商品マスタ
     */
    MastItemVo selectByItemId(@Param("itemId") String itemId);

    /**
     * 商品マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 商品マスタリスト
     */
    List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo);

    /**
     * 商品写真マスタを検索リスト
     *
     * @param itemId
     * @return
     */
    List<MastItemImageVo> selectItemImageList(String itemId);

    /**
     * 商品写真マスタを削除
     *
     * @param itemId 商品マスタID
     * @return
     */
    int deleteItemImageByItemId(String itemId);
}
