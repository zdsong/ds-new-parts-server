package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssOrderDeliveryEntity;
import jp.newpart.common.biz.mapper.BssOrderDeliveryBaseMapper;
import jp.newpart.common.biz.service.IBssOrderDeliveryBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 注文配送先情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssOrderDeliveryBaseServiceImpl implements IBssOrderDeliveryBaseService {


    @Autowired 
    private BssOrderDeliveryBaseMapper bssOrderDeliveryBaseMapper;

     /**
     *注文配送先情報を検索
     *
     * @param id 注文配送先情報プライマリ・キー
     * @return 注文配送先情報
     */
    @Override
    public BssOrderDeliveryEntity selectById(String id) {

        BssOrderDeliveryEntity  bssOrderDeliveryEntity  = bssOrderDeliveryBaseMapper.selectById(id) ;

        if (bssOrderDeliveryEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssOrderDeliveryEntity ;
    }


     /**
     * 注文配送先情報を検索リスト
     *
     * @param bssOrderDelivery 注文配送先情報パラメータ対象
     * @return 注文配送先情報リスト
     */
    @Override
    public List<BssOrderDeliveryEntity> selectList(BssOrderDeliveryEntity bssOrderDelivery) {

         return bssOrderDeliveryBaseMapper.selectList(bssOrderDelivery);
    }


     /**
     * 注文配送先情報を登録
     *
     * @param bssOrderDelivery 注文配送先情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssOrderDeliveryEntity bssOrderDelivery) {

         return bssOrderDeliveryBaseMapper.insert(bssOrderDelivery);
    }


     /**
     *注文配送先情報を修正
     *
     * @param bssOrderDelivery 注文配送先情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssOrderDeliveryEntity bssOrderDelivery) {

        int result = bssOrderDeliveryBaseMapper.update(bssOrderDelivery);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *注文配送先情報を削除
     *
     * @param id 注文配送先情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssOrderDeliveryBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
