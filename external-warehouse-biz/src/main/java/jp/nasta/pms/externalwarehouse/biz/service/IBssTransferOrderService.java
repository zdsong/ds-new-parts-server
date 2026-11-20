package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;

import java.util.List;

/**
 * 移動依頼 Serviceインタフェース
 */
public interface IBssTransferOrderService {

    /**
     * 移動依頼一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動依頼一覧
     */
    List<BssTransferListVo> selectTransferList(BssTransferSearchBo searchBo);
}
