package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssNoticeEntity;
import jp.newpart.common.biz.mapper.BssNoticeBaseMapper;
import jp.newpart.common.biz.service.IBssNoticeBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * お知らせServiceインタフェース
 *
 * @author nextosd
 */
@Service
public class BssNoticeBaseServiceImpl implements IBssNoticeBaseService {


    @Autowired 
    private BssNoticeBaseMapper bssNoticeBaseMapper;

     /**
     *お知らせを検索
     *
     * @param id お知らせプライマリ・キー
     * @return お知らせ
     */
    @Override
    public BssNoticeEntity selectById(String id) {

        BssNoticeEntity  bssNoticeEntity  = bssNoticeBaseMapper.selectById(id) ;

        if (bssNoticeEntity  == null) {
            throw new ServiceException(MessageUtils.message("EM009"));
        }
        return bssNoticeEntity ;
    }


     /**
     * お知らせを検索リスト
     *
     * @param bssNotice お知らせパラメータ対象
     * @return お知らせリスト
     */
    @Override
    public List<BssNoticeEntity> selectList(BssNoticeEntity bssNotice) {

         return bssNoticeBaseMapper.selectList(bssNotice);
    }


     /**
     * お知らせを登録
     *
     * @param bssNotice お知らせ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BssNoticeEntity bssNotice) {

         return bssNoticeBaseMapper.insert(bssNotice);
    }


     /**
     *お知らせを修正
     *
     * @param bssNotice お知らせ
     * @return  結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BssNoticeEntity bssNotice) {

        int result = bssNoticeBaseMapper.update(bssNotice);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }
     /**
     *お知らせを削除
     *
     * @param id お知らせプライマリ・キー
     * @return  結果
     */
    @Override
    public int deleteById(String id) {

        int result = bssNoticeBaseMapper.deleteById(id);
        if (result < 1) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }


}
