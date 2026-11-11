package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssAskMessageTitleEntity;
import jp.newpart.common.biz.mapper.BssAskMessageTitleBaseMapper;
import jp.newpart.common.biz.service.IBssAskMessageTitleBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * メッセージのタイトルServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssAskMessageTitleBaseServiceImpl implements IBssAskMessageTitleBaseService {


    @Autowired 
    private BssAskMessageTitleBaseMapper bssAskMessageTitleBaseMapper;

     /**
     *メッセージのタイトルを検索
     *
     * @param id メッセージのタイトルプライマリ・キー
     * @return メッセージのタイトル
     */
    @Override
    public BssAskMessageTitleEntity selectById(String id) {

        BssAskMessageTitleEntity  bssAskMessageTitleEntity  = bssAskMessageTitleBaseMapper.selectById(id) ;

        if (bssAskMessageTitleEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return bssAskMessageTitleEntity ;
    }


     /**
     * メッセージのタイトルを検索リスト
     *
     * @param bssAskMessageTitle メッセージのタイトルパラメータ対象
     * @return メッセージのタイトルリスト
     */
    @Override
    public List<BssAskMessageTitleEntity> selectList(BssAskMessageTitleEntity bssAskMessageTitle) {

         return bssAskMessageTitleBaseMapper.selectList(bssAskMessageTitle);
    }


     /**
     * メッセージのタイトルを登録
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssAskMessageTitleEntity bssAskMessageTitle) {

         return bssAskMessageTitleBaseMapper.insert(bssAskMessageTitle);
    }


     /**
     *メッセージのタイトルを修正
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssAskMessageTitleEntity bssAskMessageTitle) {

        int result = bssAskMessageTitleBaseMapper.update(bssAskMessageTitle);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *メッセージのタイトルを削除
     *
     * @param id メッセージのタイトルプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssAskMessageTitleBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
