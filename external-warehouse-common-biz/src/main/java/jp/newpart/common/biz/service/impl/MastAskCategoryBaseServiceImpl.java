package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.MastAskCategoryEntity;
import jp.newpart.common.biz.mapper.MastAskCategoryBaseMapper;
import jp.newpart.common.biz.service.IMastAskCategoryBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 問合せカテゴリServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class MastAskCategoryBaseServiceImpl implements IMastAskCategoryBaseService {


    @Autowired 
    private MastAskCategoryBaseMapper mastAskCategoryBaseMapper;

     /**
     *問合せカテゴリを検索
     *
     * @param id 問合せカテゴリプライマリ・キー
     * @return 問合せカテゴリ
     */
    @Override
    public MastAskCategoryEntity selectById(String id) {

        MastAskCategoryEntity  mastAskCategoryEntity  = mastAskCategoryBaseMapper.selectById(id) ;

        if (mastAskCategoryEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return mastAskCategoryEntity ;
    }


     /**
     * 問合せカテゴリを検索リスト
     *
     * @param mastAskCategory 問合せカテゴリパラメータ対象
     * @return 問合せカテゴリリスト
     */
    @Override
    public List<MastAskCategoryEntity> selectList(MastAskCategoryEntity mastAskCategory) {

         return mastAskCategoryBaseMapper.selectList(mastAskCategory);
    }


     /**
     * 問合せカテゴリを登録
     *
     * @param mastAskCategory 問合せカテゴリ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MastAskCategoryEntity mastAskCategory) {

         return mastAskCategoryBaseMapper.insert(mastAskCategory);
    }


     /**
     *問合せカテゴリを修正
     *
     * @param mastAskCategory 問合せカテゴリ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MastAskCategoryEntity mastAskCategory) {

        int result = mastAskCategoryBaseMapper.update(mastAskCategory);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *問合せカテゴリを削除
     *
     * @param id 問合せカテゴリプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = mastAskCategoryBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
