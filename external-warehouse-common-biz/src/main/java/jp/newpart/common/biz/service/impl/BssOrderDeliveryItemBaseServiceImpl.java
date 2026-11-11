package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssOrderDeliveryItemEntity;
import jp.newpart.common.biz.mapper.BssOrderDeliveryItemBaseMapper;
import jp.newpart.common.biz.service.IBssOrderDeliveryItemBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 注文配送先商品情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssOrderDeliveryItemBaseServiceImpl implements IBssOrderDeliveryItemBaseService {


    @Autowired 
    private BssOrderDeliveryItemBaseMapper bssOrderDeliveryItemBaseMapper;

     /**
     *注文配送先商品情報を検索
     *
     * @param id 注文配送先商品情報プライマリ・キー
     * @return 注文配送先商品情報
     */
    @Override
    public BssOrderDeliveryItemEntity selectById(String id) {

        BssOrderDeliveryItemEntity  bssOrderDeliveryItemEntity  = bssOrderDeliveryItemBaseMapper.selectById(id) ;

        if (bssOrderDeliveryItemEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssOrderDeliveryItemEntity ;
    }


     /**
     * 注文配送先商品情報を検索リスト
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報パラメータ対象
     * @return 注文配送先商品情報リスト
     */
    @Override
    public List<BssOrderDeliveryItemEntity> selectList(BssOrderDeliveryItemEntity bssOrderDeliveryItem) {

         return bssOrderDeliveryItemBaseMapper.selectList(bssOrderDeliveryItem);
    }


     /**
     * 注文配送先商品情報を登録
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssOrderDeliveryItemEntity bssOrderDeliveryItem) {

         return bssOrderDeliveryItemBaseMapper.insert(bssOrderDeliveryItem);
    }


     /**
     *注文配送先商品情報を修正
     *
     * @param bssOrderDeliveryItem 注文配送先商品情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssOrderDeliveryItemEntity bssOrderDeliveryItem) {

        int result = bssOrderDeliveryItemBaseMapper.update(bssOrderDeliveryItem);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *注文配送先商品情報を削除
     *
     * @param id 注文配送先商品情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssOrderDeliveryItemBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
}
