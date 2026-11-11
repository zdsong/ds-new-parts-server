package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastItemExtendsEntity;
import jp.newpart.common.biz.mapper.MastItemExtendsBaseMapper;
import jp.newpart.common.biz.service.IMastItemExtendsBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * 商品拡張マスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastItemExtendsBaseServiceImpl implements IMastItemExtendsBaseService {


    @Autowired 
    private MastItemExtendsBaseMapper mastItemExtendsBaseMapper;

     /**
     *商品拡張マスタを検索
     *
     * @param id 商品拡張マスタプライマリ・キー
     * @return 商品拡張マスタ
     */
    @Override
    public MastItemExtendsEntity selectById(String id) {

        MastItemExtendsEntity  mastItemExtendsEntity  = mastItemExtendsBaseMapper.selectById(id) ;

        if (mastItemExtendsEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return mastItemExtendsEntity ;
    }


     /**
     * 商品拡張マスタを検索リスト
     *
     * @param mastItemExtends 商品拡張マスタパラメータ対象
     * @return 商品拡張マスタリスト
     */
    @Override
    public List<MastItemExtendsEntity> selectList(MastItemExtendsEntity mastItemExtends) {

         return mastItemExtendsBaseMapper.selectList(mastItemExtends);
    }


     /**
     * 商品拡張マスタを登録
     *
     * @param mastItemExtends 商品拡張マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastItemExtendsEntity mastItemExtends) {

         return mastItemExtendsBaseMapper.insert(mastItemExtends);
    }


     /**
     *商品拡張マスタを修正
     *
     * @param mastItemExtends 商品拡張マスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastItemExtendsEntity mastItemExtends) {

        int result = mastItemExtendsBaseMapper.update(mastItemExtends);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *商品拡張マスタを削除
     *
     * @param id 商品拡張マスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastItemExtendsBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
