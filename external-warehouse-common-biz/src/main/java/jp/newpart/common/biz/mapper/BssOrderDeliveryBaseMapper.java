package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssOrderDeliveryEntity;

import java.util.List;


/**
 * 注文配送先情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssOrderDeliveryBaseMapper {

     /**
     *注文配送先情報を検索
     *
     * @param id 注文配送先情報プライマリ・キー
     * @return 注文配送先情報
     */
    public BssOrderDeliveryEntity selectById(String id);

     /**
     *注文配送先情報を検索リスト
     *
     * @param bssOrderDelivery 注文配送先情報
     * @return 注文配送先情報リスト
     */
    public List<BssOrderDeliveryEntity> selectList(BssOrderDeliveryEntity bssOrderDelivery);

     /**
     *注文配送先情報を登録
     *
     * @param bssOrderDelivery 注文配送先情報
     * @return  結果
     */
    public int insert(BssOrderDeliveryEntity bssOrderDelivery);

     /**
     *注文配送先情報を修正
     *
     * @param bssOrderDelivery 注文配送先情報
     * @return  結果
     */
    public int update(BssOrderDeliveryEntity bssOrderDelivery);

     /**
     * 注文配送先情報を削除
     *
     * @param id 注文配送先情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
