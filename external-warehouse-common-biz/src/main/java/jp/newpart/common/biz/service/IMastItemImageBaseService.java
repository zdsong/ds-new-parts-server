package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastItemImageEntity;
import java.util.List;


/**
 * 商品写真マスタServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastItemImageBaseService {

     /**
     * 商品写真マスタを検索
     *
     * @param id 商品写真マスタプライマリ・キー
     * @return 商品写真マスタ
     */
    public MastItemImageEntity selectById(String id);

     /**
     * 商品写真マスタを検索リスト
     *
     * @param mastItemImage 商品写真マスタパラメータ対象
     * @return 商品写真マスタリスト
     */
    public List<MastItemImageEntity> selectList(MastItemImageEntity mastItemImage);

     /**
     * 商品写真マスタを登録
     *
     * @param mastItemImage 商品写真マスタ
     * @return  結果
     */
    public int insert(MastItemImageEntity mastItemImage);

     /**
     * 商品写真マスタを修正
     *
     * @param mastItemImage 商品写真マスタ
     * @return  結果
     */
    public int update(MastItemImageEntity mastItemImage);

     /**
     * 商品写真マスタを削除
     *
     * @param id 商品写真マスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
