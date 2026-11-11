package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastItemImageEntity;
import jp.newpart.common.biz.mapper.MastItemImageBaseMapper;
import jp.newpart.common.biz.service.IMastItemImageBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 商品写真マスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastItemImageBaseServiceImpl implements IMastItemImageBaseService {


    @Autowired 
    private MastItemImageBaseMapper mastItemImageBaseMapper;

     /**
     *商品写真マスタを検索
     *
     * @param id 商品写真マスタプライマリ・キー
     * @return 商品写真マスタ
     */
    @Override
    public MastItemImageEntity selectById(String id) {

        MastItemImageEntity  mastItemImageEntity  = mastItemImageBaseMapper.selectById(id) ;

        if (mastItemImageEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return mastItemImageEntity ;
    }


     /**
     * 商品写真マスタを検索リスト
     *
     * @param mastItemImage 商品写真マスタパラメータ対象
     * @return 商品写真マスタリスト
     */
    @Override
    public List<MastItemImageEntity> selectList(MastItemImageEntity mastItemImage) {

         return mastItemImageBaseMapper.selectList(mastItemImage);
    }


     /**
     * 商品写真マスタを登録
     *
     * @param mastItemImage 商品写真マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastItemImageEntity mastItemImage) {

         return mastItemImageBaseMapper.insert(mastItemImage);
    }


     /**
     *商品写真マスタを修正
     *
     * @param mastItemImage 商品写真マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastItemImageEntity mastItemImage) {

        int result = mastItemImageBaseMapper.update(mastItemImage);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *商品写真マスタを削除
     *
     * @param id 商品写真マスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastItemImageBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
