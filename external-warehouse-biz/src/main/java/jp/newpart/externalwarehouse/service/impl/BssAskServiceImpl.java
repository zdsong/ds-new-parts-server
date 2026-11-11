package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.externalwarehouse.biz.domain.vo.BssAskVo;
import jp.newpart.externalwarehouse.biz.mapper.BssAskMapper;
import jp.newpart.externalwarehouse.biz.service.IBssAskService;
import jp.newpart.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * お問い合わせ Service業務処理
 *
 * @author nextosd
 */
@Service
public class BssAskServiceImpl implements IBssAskService {

    @Autowired
    private BssAskMapper bssAskMapper;

    /**
     * 注文番号による問い合わせ履歴を検索
     *
     * @param orderId 注文番号
     * @return 問い合わせ履歴情報
     */
    @Override
    public List<BssAskVo> getAskListHistoryByOrderId(String orderId) {
        return bssAskMapper.selectAskListHistoryByOrderId(orderId, SecurityUtils.getLoginUser().getUsername());
    }
}
