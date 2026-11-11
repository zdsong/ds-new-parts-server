package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssAskHistoryEntity;
import jp.newpart.common.biz.mapper.BssAskHistoryBaseMapper;
import jp.newpart.common.biz.service.IBssAskHistoryBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 問合せ対応履歴情報 Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssAskHistoryBaseServiceImpl implements IBssAskHistoryBaseService {


    @Autowired 
    private BssAskHistoryBaseMapper bssAskHistoryBaseMapper;

     /**
     *問合せ対応履歴情報 を検索
     *
     * @param id 問合せ対応履歴情報 プライマリ・キー
     * @return 問合せ対応履歴情報 
     */
    @Override
    public BssAskHistoryEntity selectById(String id) {

        BssAskHistoryEntity  bssAskHistoryEntity  = bssAskHistoryBaseMapper.selectById(id) ;

        if (bssAskHistoryEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssAskHistoryEntity ;
    }


     /**
     * 問合せ対応履歴情報 を検索リスト
     *
     * @param bssAskHistory 問合せ対応履歴情報 パラメータ対象
     * @return 問合せ対応履歴情報 リスト
     */
    @Override
    public List<BssAskHistoryEntity> selectList(BssAskHistoryEntity bssAskHistory) {

         return bssAskHistoryBaseMapper.selectList(bssAskHistory);
    }


     /**
     * 問合せ対応履歴情報 を登録
     *
     * @param bssAskHistory 問合せ対応履歴情報 
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssAskHistoryEntity bssAskHistory) {

         return bssAskHistoryBaseMapper.insert(bssAskHistory);
    }


     /**
     *問合せ対応履歴情報 を修正
     *
     * @param bssAskHistory 問合せ対応履歴情報 
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssAskHistoryEntity bssAskHistory) {

        int result = bssAskHistoryBaseMapper.update(bssAskHistory);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *問合せ対応履歴情報 を削除
     *
     * @param id 問合せ対応履歴情報 プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssAskHistoryBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
