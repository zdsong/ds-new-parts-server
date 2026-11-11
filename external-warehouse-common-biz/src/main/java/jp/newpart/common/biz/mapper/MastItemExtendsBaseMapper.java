package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.MastItemExtendsEntity;

import java.util.List;


/**
 * 商品拡張マスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastItemExtendsBaseMapper {

     /**
     *商品拡張マスタを検索
     *
     * @param id 商品拡張マスタプライマリ・キー
     * @return 商品拡張マスタ
     */
    public MastItemExtendsEntity selectById(String id);

     /**
     *商品拡張マスタを検索リスト
     *
     * @param mastItemExtends 商品拡張マスタ
     * @return 商品拡張マスタリスト
     */
    public List<MastItemExtendsEntity> selectList(MastItemExtendsEntity mastItemExtends);

     /**
     *商品拡張マスタを登録
     *
     * @param mastItemExtends 商品拡張マスタ
     * @return  結果
     */
    public int insert(MastItemExtendsEntity mastItemExtends);

     /**
     *商品拡張マスタを修正
     *
     * @param mastItemExtends 商品拡張マスタ
     * @return  結果
     */
    public int update(MastItemExtendsEntity mastItemExtends);

     /**
     * 商品拡張マスタを削除
     *
     * @param id 商品拡張マスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
