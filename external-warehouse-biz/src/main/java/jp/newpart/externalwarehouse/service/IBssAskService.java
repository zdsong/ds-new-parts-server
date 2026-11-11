package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.externalwarehouse.biz.domain.vo.BssAskVo;

import java.util.List;

/**
 * 問合せ情報
 */
public interface IBssAskService {

    /**
     * 注文番号による問い合わせ履歴を検索
     *
     * @param orderId 注文番号
     * @return 問い合わせ履歴情報
     */
    List<BssAskVo> getAskListHistoryByOrderId(String orderId);
}
