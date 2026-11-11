package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastItemEntity;
import java.util.List;


/**
 * 商品マスタServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastItemBaseService {

     /**
     * 商品マスタを検索
     *
     * @param id 商品マスタプライマリ・キー
     * @return 商品マスタ
     */
    public MastItemEntity selectById(String id);

     /**
     * 商品マスタを検索リスト
     *
     * @param mastItem 商品マスタパラメータ対象
     * @return 商品マスタリスト
     */
    public List<MastItemEntity> selectList(MastItemEntity mastItem);

     /**
     * 商品マスタを登録
     *
     * @param mastItem 商品マスタ
     * @return  結果
     */
    public int insert(MastItemEntity mastItem);

     /**
     * 商品マスタを修正
     *
     * @param mastItem 商品マスタ
     * @return  結果
     */
    public int update(MastItemEntity mastItem);

     /**
     * 商品マスタを削除
     *
     * @param id 商品マスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
