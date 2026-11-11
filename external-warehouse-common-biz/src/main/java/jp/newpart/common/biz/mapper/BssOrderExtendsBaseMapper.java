package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssOrderExtendsEntity;

import java.util.List;


/**
 * 注文拡情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssOrderExtendsBaseMapper {

     /**
     *注文拡情報を検索
     *
     * @param id 注文拡情報プライマリ・キー
     * @return 注文拡情報
     */
    public BssOrderExtendsEntity selectById(String id);

     /**
     *注文拡情報を検索リスト
     *
     * @param bssOrderExtends 注文拡情報
     * @return 注文拡情報リスト
     */
    public List<BssOrderExtendsEntity> selectList(BssOrderExtendsEntity bssOrderExtends);

     /**
     *注文拡情報を登録
     *
     * @param bssOrderExtends 注文拡情報
     * @return  結果
     */
    public int insert(BssOrderExtendsEntity bssOrderExtends);

     /**
     *注文拡情報を修正
     *
     * @param bssOrderExtends 注文拡情報
     * @return  結果
     */
    public int update(BssOrderExtendsEntity bssOrderExtends);

     /**
     * 注文拡情報を削除
     *
     * @param id 注文拡情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
