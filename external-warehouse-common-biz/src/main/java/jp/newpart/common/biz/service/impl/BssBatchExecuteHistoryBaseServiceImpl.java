package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssBatchExecuteHistoryEntity;
import jp.newpart.common.biz.mapper.BssBatchExecuteHistoryBaseMapper;
import jp.newpart.common.biz.service.IBssBatchExecuteHistoryBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * バッチ実行履歴Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssBatchExecuteHistoryBaseServiceImpl implements IBssBatchExecuteHistoryBaseService {


    @Autowired 
    private BssBatchExecuteHistoryBaseMapper bssBatchExecuteHistoryBaseMapper;

     /**
     *バッチ実行履歴を検索
     *
     * @param id バッチ実行履歴プライマリ・キー
     * @return バッチ実行履歴
     */
    @Override
    public BssBatchExecuteHistoryEntity selectById(String id) {

        BssBatchExecuteHistoryEntity  bssBatchExecuteHistoryEntity  = bssBatchExecuteHistoryBaseMapper.selectById(id) ;

        if (bssBatchExecuteHistoryEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssBatchExecuteHistoryEntity ;
    }


     /**
     * バッチ実行履歴を検索リスト
     *
     * @param bssBatchExecuteHistory バッチ実行履歴パラメータ対象
     * @return バッチ実行履歴リスト
     */
    @Override
    public List<BssBatchExecuteHistoryEntity> selectList(BssBatchExecuteHistoryEntity bssBatchExecuteHistory) {

         return bssBatchExecuteHistoryBaseMapper.selectList(bssBatchExecuteHistory);
    }


     /**
     * バッチ実行履歴を登録
     *
     * @param bssBatchExecuteHistory バッチ実行履歴
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssBatchExecuteHistoryEntity bssBatchExecuteHistory) {

         return bssBatchExecuteHistoryBaseMapper.insert(bssBatchExecuteHistory);
    }


     /**
     *バッチ実行履歴を修正
     *
     * @param bssBatchExecuteHistory バッチ実行履歴
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssBatchExecuteHistoryEntity bssBatchExecuteHistory) {

        int result = bssBatchExecuteHistoryBaseMapper.update(bssBatchExecuteHistory);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *バッチ実行履歴を削除
     *
     * @param id バッチ実行履歴プライマリ・キー
     * @return  結果
     */
    @Override
     public int deleteById(String id) {

         int result = bssBatchExecuteHistoryBaseMapper.deleteById(id);
         if (result < 1) {
             throw new ServiceException(MessageUtils.message("E0007"));
         }
         return result;
     }

     @Override
     public List<BssBatchExecuteHistoryEntity> getBatchExecuteHistoryByCooperationDate(String cooperationDate, String createBy) {
         java.util.Map<String, Object> params = new java.util.HashMap<>();
         params.put("cooperationDate", cooperationDate);
         params.put("createBy", createBy);
         return bssBatchExecuteHistoryBaseMapper.getBatchExecuteHistoryByCooperationDate(params);
     }

}
