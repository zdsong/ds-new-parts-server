package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssOrderExtendsEntity;
import jp.newpart.common.biz.mapper.BssOrderExtendsBaseMapper;
import jp.newpart.common.biz.service.IBssOrderExtendsBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 注文拡情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssOrderExtendsBaseServiceImpl implements IBssOrderExtendsBaseService {


    @Autowired 
    private BssOrderExtendsBaseMapper bssOrderExtendsBaseMapper;

     /**
     *注文拡情報を検索
     *
     * @param id 注文拡情報プライマリ・キー
     * @return 注文拡情報
     */
    @Override
    public BssOrderExtendsEntity selectById(String id) {

        BssOrderExtendsEntity  bssOrderExtendsEntity  = bssOrderExtendsBaseMapper.selectById(id) ;

        if (bssOrderExtendsEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return bssOrderExtendsEntity ;
    }


     /**
     * 注文拡情報を検索リスト
     *
     * @param bssOrderExtends 注文拡情報パラメータ対象
     * @return 注文拡情報リスト
     */
    @Override
    public List<BssOrderExtendsEntity> selectList(BssOrderExtendsEntity bssOrderExtends) {

         return bssOrderExtendsBaseMapper.selectList(bssOrderExtends);
    }


     /**
     * 注文拡情報を登録
     *
     * @param bssOrderExtends 注文拡情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssOrderExtendsEntity bssOrderExtends) {

         return bssOrderExtendsBaseMapper.insert(bssOrderExtends);
    }


     /**
     *注文拡情報を修正
     *
     * @param bssOrderExtends 注文拡情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssOrderExtendsEntity bssOrderExtends) {

        int result = bssOrderExtendsBaseMapper.update(bssOrderExtends);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *注文拡情報を削除
     *
     * @param id 注文拡情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssOrderExtendsBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
