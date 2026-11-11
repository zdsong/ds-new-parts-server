package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderItemCustomerVo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderListVo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssOrderSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 注文情報 Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssOrderService {

    /**
     * 注文情報一覧の検索
     *
     * @param orderSearchBo 注文情報検索条件
     * @return 注文情報一覧
     */
    List<BssOrderListVo> selectList(BssOrderSearchBo orderSearchBo);

    /**
     * 注文情報一覧の検索
     * 注文配送先情報Group
     * @param orderSearchBo 注文情報検索条件
     * @return 注文情報一覧
     */
    List<BssOrderListVo> selectListDeliveryId(BssOrderSearchBo orderSearchBo);

    /**
     * 注文詳細情報
     *
     * @param orderId 注文ID
     * @return 注文情報
     */
    BssOrderInfoVo selectOrderByOrderId(String orderId);

    /**
     * 顧客購入履歴
     * @param customerId 顧客ID
     * @return
     */
    List<BssOrderItemCustomerVo> selectOrderItemListByCustomerId(@Param("customerId") String customerId);
}
