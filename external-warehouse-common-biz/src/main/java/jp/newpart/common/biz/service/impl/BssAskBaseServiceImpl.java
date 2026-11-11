package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.service.IBssAskBaseService;
import jp.newpart.common.biz.domain.entity.BssAskEntity;
import jp.newpart.common.biz.mapper.BssAskBaseMapper;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 問合せ情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssAskBaseServiceImpl implements IBssAskBaseService {


    @Autowired 
    private BssAskBaseMapper bssAskBaseMapper;

     /**
     *問合せ情報を検索
     *
     * @param id 問合せ情報プライマリ・キー
     * @return 問合せ情報
     */
    @Override
    public BssAskEntity selectById(String id) {

        BssAskEntity  bssAskEntity  = bssAskBaseMapper.selectById(id) ;

        if (bssAskEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssAskEntity ;
    }


     /**
     * 問合せ情報を検索リスト
     *
     * @param bssAsk 問合せ情報パラメータ対象
     * @return 問合せ情報リスト
     */
    @Override
    public List<BssAskEntity> selectList(BssAskEntity bssAsk) {

         return bssAskBaseMapper.selectList(bssAsk);
    }


     /**
     * 問合せ情報を登録
     *
     * @param bssAsk 問合せ情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssAskEntity bssAsk) {

         return bssAskBaseMapper.insert(bssAsk);
    }


     /**
     *問合せ情報を修正
     *
     * @param bssAsk 問合せ情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssAskEntity bssAsk) {

        int result = bssAskBaseMapper.update(bssAsk);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *問合せ情報を削除
     *
     * @param id 問合せ情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssAskBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
