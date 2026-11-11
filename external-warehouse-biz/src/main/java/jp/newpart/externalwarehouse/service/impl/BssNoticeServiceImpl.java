package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssNoticeEntity;
import jp.newpart.common.biz.enums.BssNoticeStatus;
import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.biz.service.IBssNoticeBaseService;
import jp.newpart.externalwarehouse.biz.domain.vo.BssNoticeVo;
import jp.newpart.externalwarehouse.biz.mapper.BssNoticeMapper;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.externalwarehouse.biz.service.IBssNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * お知らせ Service業務処理
 *
 * @author nextosd
 */
@Service
@Slf4j
public class BssNoticeServiceImpl implements IBssNoticeService {

    @Autowired
    private BssNoticeMapper bssNoticeMapper;

    @Autowired
    private IBssNoticeBaseService bssNoticeBaseService;

    /**
     * 未読お知らせ数取得
     *
     * @return
     */
    @Override
    public int getUnReadNoticeCnt() {
        return bssNoticeMapper.selectUnReadNoticeCnt(SecurityUtils.getLoginUser().getUsername());
    }

    /**
     * お知らせ情報を検索リスト
     *
     * @return お知らせ情報リスト
     */
    @Override
    public List<BssNoticeVo> getUnReadNoticeList() {
        return bssNoticeMapper.selectUnReadNoticeList(SecurityUtils.getLoginUser().getUsername());
    }

    /**
     * お知らせ既読
     *
     * @param id お知らせID
     * @return  結果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int readNotice(String id) {
        BssNoticeEntity bssNoticeEntity = bssNoticeBaseService.selectById(id);
        bssNoticeEntity.setStatus(BssNoticeStatus.READ.getCode());
        bssNoticeEntity.setId(id);
        bssNoticeEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N017.getCode());
        // 受信日時
        bssNoticeEntity.setReadTime(DateUtils.getTime());

        return bssNoticeBaseService.update(bssNoticeEntity);
    }
}
