package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssOrderEntity;
import jp.newpart.common.biz.mapper.BssOrderBaseMapper;
import jp.newpart.common.biz.service.IBssOrderBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 注文情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssOrderBaseServiceImpl implements IBssOrderBaseService {


    @Autowired 
    private BssOrderBaseMapper bssOrderBaseMapper;

     /**
     *注文情報を検索
     *
     * @param id 注文情報プライマリ・キー
     * @return 注文情報
     */
    @Override
    public BssOrderEntity selectById(String id) {

        BssOrderEntity  bssOrderEntity  = bssOrderBaseMapper.selectById(id) ;

        if (bssOrderEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssOrderEntity ;
    }


     /**
     * 注文情報を検索リスト
     *
     * @param bssOrder 注文情報パラメータ対象
     * @return 注文情報リスト
     */
    @Override
    public List<BssOrderEntity> selectList(BssOrderEntity bssOrder) {

         return bssOrderBaseMapper.selectList(bssOrder);
    }

     /**
     * 注文情報を登録
     *
     * @param bssOrder 注文情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssOrderEntity bssOrder) {

         return bssOrderBaseMapper.insert(bssOrder);
    }


     /**
     *注文情報を修正
     *
     * @param bssOrder 注文情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssOrderEntity bssOrder) {

        int result = bssOrderBaseMapper.update(bssOrder);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *注文情報を削除
     *
     * @param id 注文情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssOrderBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
