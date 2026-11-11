package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.service.IBssOrderStatusHistoryBaseService;
import jp.newpart.common.biz.domain.entity.BssOrderStatusHistoryEntity;
import jp.newpart.common.biz.mapper.BssOrderStatusHistoryBaseMapper;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 注文ステータス履歴情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssOrderStatusHistoryBaseServiceImpl implements IBssOrderStatusHistoryBaseService {


    @Autowired 
    private BssOrderStatusHistoryBaseMapper bssOrderStatusHistoryBaseMapper;

     /**
     *注文ステータス履歴情報を検索
     *
     * @param id 注文ステータス履歴情報プライマリ・キー
     * @return 注文ステータス履歴情報
     */
    @Override
    public BssOrderStatusHistoryEntity selectById(String id) {

        BssOrderStatusHistoryEntity  bssOrderStatusHistoryEntity  = bssOrderStatusHistoryBaseMapper.selectById(id) ;

        if (bssOrderStatusHistoryEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssOrderStatusHistoryEntity ;
    }


     /**
     * 注文ステータス履歴情報を検索リスト
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報パラメータ対象
     * @return 注文ステータス履歴情報リスト
     */
    @Override
    public List<BssOrderStatusHistoryEntity> selectList(BssOrderStatusHistoryEntity bssOrderStatusHistory) {

         return bssOrderStatusHistoryBaseMapper.selectList(bssOrderStatusHistory);
    }


     /**
     * 注文ステータス履歴情報を登録
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssOrderStatusHistoryEntity bssOrderStatusHistory) {

         return bssOrderStatusHistoryBaseMapper.insert(bssOrderStatusHistory);
    }


     /**
     *注文ステータス履歴情報を修正
     *
     * @param bssOrderStatusHistory 注文ステータス履歴情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssOrderStatusHistoryEntity bssOrderStatusHistory) {

        int result = bssOrderStatusHistoryBaseMapper.update(bssOrderStatusHistory);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *注文ステータス履歴情報を削除
     *
     * @param id 注文ステータス履歴情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssOrderStatusHistoryBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
