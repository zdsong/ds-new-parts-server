package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastFarmerRakuuruCartEntity;
import jp.newpart.common.biz.mapper.MastFarmerRakuuruCartBaseMapper;
import jp.newpart.common.biz.service.IMastFarmerRakuuruCartBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 農家らくうるカートマスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastFarmerRakuuruCartBaseServiceImpl implements IMastFarmerRakuuruCartBaseService {


    @Autowired 
    private MastFarmerRakuuruCartBaseMapper mastFarmerRakuuruCartBaseMapper;

     /**
     *農家らくうるカートマスタを検索
     *
     * @param id 農家らくうるカートマスタプライマリ・キー
     * @return 農家らくうるカートマスタ
     */
    @Override
    public MastFarmerRakuuruCartEntity selectById(String id) {

        MastFarmerRakuuruCartEntity  mastFarmerRakuuruCartEntity  = mastFarmerRakuuruCartBaseMapper.selectById(id) ;

        if (mastFarmerRakuuruCartEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return mastFarmerRakuuruCartEntity ;
    }


     /**
     * 農家らくうるカートマスタを検索リスト
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタパラメータ対象
     * @return 農家らくうるカートマスタリスト
     */
    @Override
    public List<MastFarmerRakuuruCartEntity> selectList(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart) {

         return mastFarmerRakuuruCartBaseMapper.selectList(mastFarmerRakuuruCart);
    }


     /**
     * 農家らくうるカートマスタを登録
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart) {

         return mastFarmerRakuuruCartBaseMapper.insert(mastFarmerRakuuruCart);
    }


     /**
     *農家らくうるカートマスタを修正
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart) {

        int result = mastFarmerRakuuruCartBaseMapper.update(mastFarmerRakuuruCart);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *農家らくうるカートマスタを削除
     *
     * @param id 農家らくうるカートマスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastFarmerRakuuruCartBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
