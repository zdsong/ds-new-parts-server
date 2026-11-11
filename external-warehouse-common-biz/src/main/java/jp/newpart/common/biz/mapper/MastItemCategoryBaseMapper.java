package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.MastItemCategoryEntity;

import java.util.List;


/**
 * 商品カテゴリマスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastItemCategoryBaseMapper {

     /**
     *商品カテゴリマスタを検索
     *
     * @param id 商品カテゴリマスタプライマリ・キー
     * @return 商品カテゴリマスタ
     */
    public MastItemCategoryEntity selectById(String id);

     /**
     *商品カテゴリマスタを検索リスト
     *
     * @param mastItemCategory 商品カテゴリマスタ
     * @return 商品カテゴリマスタリスト
     */
    public List<MastItemCategoryEntity> selectList(MastItemCategoryEntity mastItemCategory);

     /**
     *商品カテゴリマスタを登録
     *
     * @param mastItemCategory 商品カテゴリマスタ
     * @return  結果
     */
    public int insert(MastItemCategoryEntity mastItemCategory);

     /**
     *商品カテゴリマスタを修正
     *
     * @param mastItemCategory 商品カテゴリマスタ
     * @return  結果
     */
    public int update(MastItemCategoryEntity mastItemCategory);

     /**
     * 商品カテゴリマスタを削除
     *
     * @param id 商品カテゴリマスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
