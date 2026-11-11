package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.BssOrderDeliveryItemEntity;
import java.util.List;


/**
 * 注文配送先商品情報Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssOrderDeliveryItemBaseService {

     /**
     * 注文配送先商品情報を検索
     *
     * @param id 注文配送先商品情報プライマリ・キー
     * @return 注文配送先商品情報
     */
    public BssOrderDeliveryItemEntity selectById(String id);

     /**
     * 注文配送先商品情報を検索リスト
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報パラメータ対象
     * @return 注文配送先商品情報リスト
     */
    public List<BssOrderDeliveryItemEntity> selectList(BssOrderDeliveryItemEntity bssOrderDeliveryItem);

     /**
     * 注文配送先商品情報を登録
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報
     * @return  結果
     */
    public int insert(BssOrderDeliveryItemEntity bssOrderDeliveryItem);

     /**
     * 注文配送先商品情報を修正
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報
     * @return  結果
     */
    public int update(BssOrderDeliveryItemEntity bssOrderDeliveryItem);

     /**
     * 注文配送先商品情報を削除
     *
     * @param id 注文配送先商品情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
