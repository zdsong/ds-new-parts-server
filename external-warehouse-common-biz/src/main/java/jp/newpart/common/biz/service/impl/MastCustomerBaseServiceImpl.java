package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastCustomerEntity;
import jp.newpart.common.biz.mapper.MastCustomerBaseMapper;
import jp.newpart.common.biz.service.IMastCustomerBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 顧客マスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastCustomerBaseServiceImpl implements IMastCustomerBaseService {


    @Autowired 
    private MastCustomerBaseMapper mastCustomerBaseMapper;

     /**
     *顧客マスタを検索
     *
     * @param id 顧客マスタプライマリ・キー
     * @return 顧客マスタ
     */
    @Override
    public MastCustomerEntity selectById(String id) {

        MastCustomerEntity  mastCustomerEntity  = mastCustomerBaseMapper.selectById(id) ;

        if (mastCustomerEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return mastCustomerEntity ;
    }


     /**
     * 顧客マスタを検索リスト
     *
     * @param mastCustomer 顧客マスタパラメータ対象
     * @return 顧客マスタリスト
     */
    @Override
    public List<MastCustomerEntity> selectList(MastCustomerEntity mastCustomer) {

         return mastCustomerBaseMapper.selectList(mastCustomer);
    }


     /**
     * 顧客マスタを登録
     *
     * @param mastCustomer 顧客マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastCustomerEntity mastCustomer) {

         return mastCustomerBaseMapper.insert(mastCustomer);
    }


     /**
     *顧客マスタを修正
     *
     * @param mastCustomer 顧客マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastCustomerEntity mastCustomer) {

        int result = mastCustomerBaseMapper.update(mastCustomer);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *顧客マスタを削除
     *
     * @param id 顧客マスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastCustomerBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
