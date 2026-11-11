package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssUserCertificationEntity;
import jp.newpart.common.biz.mapper.BssUserCertificationBaseMapper;
import jp.newpart.common.biz.service.IBssUserCertificationBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ユーザー認証情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssUserCertificationBaseServiceImpl implements IBssUserCertificationBaseService {


    @Autowired 
    private BssUserCertificationBaseMapper bssUserCertificationBaseMapper;

    /**
     * ユーザー認証情報を検索
     *
     * @param id ユーザー認証情報プライマリ・キー
     * @return ユーザー認証情報
     */
    @Override
    public BssUserCertificationEntity selectById(String id) {

        BssUserCertificationEntity  bssUserCertificationEntity  = bssUserCertificationBaseMapper.selectById(id) ;

        if (bssUserCertificationEntity  == null) {
            throw new ServiceException(MessageUtils.message("E0016", id));
        }
        return bssUserCertificationEntity ;
    }


    /**
     * ユーザー認証情報を検索リスト
     *
     * @param bssUserCertification ユーザー認証情報パラメータ対象
     * @return ユーザー認証情報リスト
     */
    @Override
    public List<BssUserCertificationEntity> selectList(BssUserCertificationEntity bssUserCertification) {

         return bssUserCertificationBaseMapper.selectList(bssUserCertification);
    }


    /**
     * ユーザー認証情報を登録
     *
     * @param bssUserCertification ユーザー認証情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssUserCertificationEntity bssUserCertification) {

         return bssUserCertificationBaseMapper.insert(bssUserCertification);
    }


    /**
     * ユーザー認証情報を修正
     *
     * @param bssUserCertification ユーザー認証情報
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssUserCertificationEntity bssUserCertification) {

        int result = bssUserCertificationBaseMapper.update(bssUserCertification);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * ユーザー認証情報を削除
     *
     * @param id ユーザー認証情報プライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssUserCertificationBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * 農家IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
    @Override
    public int deleteLogicByFarmId(BssUserCertificationEntity bssUserCertification){
        return bssUserCertificationBaseMapper.deleteLogicByFarmId(bssUserCertification);
    }

    /**
     * IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
    @Override
    public int deleteLogicById(BssUserCertificationEntity bssUserCertification){
        return bssUserCertificationBaseMapper.deleteLogicByFarmId(bssUserCertification);
    }
}
