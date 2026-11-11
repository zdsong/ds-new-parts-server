package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;
import jp.newpart.common.biz.mapper.BssAskMessageBaseMapper;
import jp.newpart.common.biz.service.IBssAskMessageBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 問合せメッセージServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssAskMessageBaseServiceImpl implements IBssAskMessageBaseService {


    @Autowired 
    private BssAskMessageBaseMapper bssAskMessageBaseMapper;

     /**
     *問合せメッセージを検索
     *
     * @param id 問合せメッセージプライマリ・キー
     * @return 問合せメッセージ
     */
    @Override
    public BssAskMessageEntity selectById(String id) {

        BssAskMessageEntity  bssAskMessageEntity  = bssAskMessageBaseMapper.selectById(id) ;

        if (bssAskMessageEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return bssAskMessageEntity ;
    }


     /**
     * 問合せメッセージを検索リスト
     *
     * @param bssAskMessage 問合せメッセージパラメータ対象
     * @return 問合せメッセージリスト
     */
    @Override
    public List<BssAskMessageEntity> selectList(BssAskMessageEntity bssAskMessage) {

         return bssAskMessageBaseMapper.selectList(bssAskMessage);
    }


     /**
     * 問合せメッセージを登録
     *
     * @param bssAskMessage 問合せメッセージ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssAskMessageEntity bssAskMessage) {

         return bssAskMessageBaseMapper.insert(bssAskMessage);
    }


     /**
     *問合せメッセージを修正
     *
     * @param bssAskMessage 問合せメッセージ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssAskMessageEntity bssAskMessage) {

        int result = bssAskMessageBaseMapper.update(bssAskMessage);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return result;
    }
     /**
     *問合せメッセージを削除
     *
     * @param id 問合せメッセージプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssAskMessageBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
