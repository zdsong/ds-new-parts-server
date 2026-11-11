package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastItemEntity;
import jp.newpart.common.biz.mapper.MastItemBaseMapper;
import jp.newpart.common.biz.service.IMastItemBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 商品マスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastItemBaseServiceImpl implements IMastItemBaseService {


    @Autowired 
    private MastItemBaseMapper mastItemBaseMapper;

     /**
     *商品マスタを検索
     *
     * @param id 商品マスタプライマリ・キー
     * @return 商品マスタ
     */
    @Override
    public MastItemEntity selectById(String id) {

        MastItemEntity  mastItemEntity  = mastItemBaseMapper.selectById(id) ;

        if (mastItemEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return mastItemEntity ;
    }


     /**
     * 商品マスタを検索リスト
     *
     * @param mastItem 商品マスタパラメータ対象
     * @return 商品マスタリスト
     */
    @Override
    public List<MastItemEntity> selectList(MastItemEntity mastItem) {

         return mastItemBaseMapper.selectList(mastItem);
    }


     /**
     * 商品マスタを登録
     *
     * @param mastItem 商品マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastItemEntity mastItem) {

         return mastItemBaseMapper.insert(mastItem);
    }


     /**
     *商品マスタを修正
     *
     * @param mastItem 商品マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastItemEntity mastItem) {

        int result = mastItemBaseMapper.update(mastItem);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *商品マスタを削除
     *
     * @param id 商品マスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastItemBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
}
