package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderItemCustomerVo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderListVo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssOrderSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssOrderInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 注文情報
 */
public interface BssOrderMapper {

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
     * 注文情報を検索
     *
     * @param orderId 注文ID
     * @param farmerId   農家ID
     * @return 注文情報
     */
     BssOrderInfoVo selectOrderByOrderId(@Param("orderId") String orderId, @Param("farmerId") String farmerId);

    /**
     * 顧客購入履歴
     * @param farmerId   農家ID
     * @param customerId 顧客ID
     * @return
     */
     List<BssOrderItemCustomerVo> selectOrderItemListByCustomerId(@Param("farmerId") String farmerId, @Param("customerId") String customerId);
}
