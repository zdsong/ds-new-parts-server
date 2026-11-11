package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastItemCategoryEntity;
import jp.newpart.common.biz.mapper.MastItemCategoryBaseMapper;
import jp.newpart.common.biz.service.IMastItemCategoryBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 商品カテゴリマスタServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastItemCategoryBaseServiceImpl implements IMastItemCategoryBaseService {


    @Autowired 
    private MastItemCategoryBaseMapper mastItemCategoryBaseMapper;

     /**
     *商品カテゴリマスタを検索
     *
     * @param id 商品カテゴリマスタプライマリ・キー
     * @return 商品カテゴリマスタ
     */
    @Override
    public MastItemCategoryEntity selectById(String id) {

        MastItemCategoryEntity  mastItemCategoryEntity  = mastItemCategoryBaseMapper.selectById(id) ;

        if (mastItemCategoryEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return mastItemCategoryEntity ;
    }


     /**
     * 商品カテゴリマスタを検索リスト
     *
     * @param mastItemCategory 商品カテゴリマスタパラメータ対象
     * @return 商品カテゴリマスタリスト
     */
    @Override
    public List<MastItemCategoryEntity> selectList(MastItemCategoryEntity mastItemCategory) {

         return mastItemCategoryBaseMapper.selectList(mastItemCategory);
    }


     /**
     * 商品カテゴリマスタを登録
     *
     * @param mastItemCategory 商品カテゴリマスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastItemCategoryEntity mastItemCategory) {

         return mastItemCategoryBaseMapper.insert(mastItemCategory);
    }


     /**
     *商品カテゴリマスタを修正
     *
     * @param mastItemCategory 商品カテゴリマスタ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastItemCategoryEntity mastItemCategory) {

        int result = mastItemCategoryBaseMapper.update(mastItemCategory);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *商品カテゴリマスタを削除
     *
     * @param id 商品カテゴリマスタプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastItemCategoryBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
