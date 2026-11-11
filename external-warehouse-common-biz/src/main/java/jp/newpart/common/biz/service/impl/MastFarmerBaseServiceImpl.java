package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.service.IMastFarmerBaseService;
import jp.newpart.common.biz.domain.entity.MastFarmerEntity;
import jp.newpart.common.biz.mapper.MastFarmerBaseMapper;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 農家マスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastFarmerBaseServiceImpl implements IMastFarmerBaseService {


    @Autowired 
    private MastFarmerBaseMapper mastFarmerBaseMapper;

     /**
     *農家マスタを検索
     *
     * @param id 農家マスタプライマリ・キー
     * @return 農家マスタ
     */
    @Override
    public MastFarmerEntity selectById(String id) {

        MastFarmerEntity  mastFarmerEntity  = mastFarmerBaseMapper.selectById(id) ;

        if (mastFarmerEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return mastFarmerEntity ;
    }


     /**
     * 農家マスタを検索リスト
     *
     * @param mastFarmer 農家マスタパラメータ対象
     * @return 農家マスタリスト
     */
    @Override
    public List<MastFarmerEntity> selectList(MastFarmerEntity mastFarmer) {

         return mastFarmerBaseMapper.selectList(mastFarmer);
    }


     /**
     * 農家マスタを登録
     *
     * @param mastFarmer 農家マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastFarmerEntity mastFarmer) {

         return mastFarmerBaseMapper.insert(mastFarmer);
    }


     /**
     *農家マスタを修正
     *
     * @param mastFarmer 農家マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastFarmerEntity mastFarmer) {

        int result = mastFarmerBaseMapper.update(mastFarmer);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *農家マスタを削除
     *
     * @param id 農家マスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastFarmerBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
