package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssOrderDeliveryEntity;
import jp.newpart.common.biz.domain.entity.BssOrderDeliveryItemEntity;
import jp.newpart.common.biz.service.IBssOrderDeliveryBaseService;
import jp.newpart.common.biz.service.IBssOrderDeliveryItemBaseService;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.bean.BeanUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.BssOrderSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.*;
import jp.newpart.externalwarehouse.biz.mapper.BssOrderMapper;
import jp.newpart.externalwarehouse.biz.service.IBssOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 注文 Service業務処理
 *
 * @author nextosd
 */
@Service
public class BssOrderServiceImpl implements IBssOrderService {

    @Autowired
    private BssOrderMapper bssOrderMapper;

    @Autowired
    private IBssOrderDeliveryItemBaseService bssOrderDeliveryItemBaseService;

    @Autowired
    private IBssOrderDeliveryBaseService bssOrderDeliveryBaseService;

    /**
     * 注文情報一覧の検索
     *
     * @param orderSearchBo 注文情報検索条件
     * @return 注文情報一覧
     */
    @Override
    public List<BssOrderListVo> selectList(BssOrderSearchBo orderSearchBo) {
        orderSearchBo.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        List<BssOrderListVo> orders = bssOrderMapper.selectList(orderSearchBo);
        // PCクエリ
        if (ValueUtils.isNotEmpty(orderSearchBo.getPcSearchFlg()) && orderSearchBo.getPcSearchFlg()) {
            this.mergeOrders(orders);
        }

        return orders;
    }

    /**
     * 注文情報一覧の検索
     * 注文配送先情報Group
     * @param orderSearchBo 注文情報検索条件
     * @return 注文情報一覧
     */
    @Override
    public List<BssOrderListVo> selectListDeliveryId(BssOrderSearchBo orderSearchBo){
        orderSearchBo.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        return bssOrderMapper.selectListDeliveryId(orderSearchBo);
    }

    /**
     * 注文詳細情報
     *
     * @param orderId 注文ID
     * @return 注文情報
     */
    @Override
    public BssOrderInfoVo selectOrderByOrderId(String orderId) {

        String farmerId = SecurityUtils.getLoginUser().getUsername();

        // 注文情報を検索
        BssOrderInfoVo bssOrderInfoVo = bssOrderMapper.selectOrderByOrderId(orderId, farmerId);

        // 農家ID
        if(bssOrderInfoVo == null){
            throw new ServiceException(MessageUtils.message("E0064", orderId));
        }
        BigDecimal totalCarriage = BigDecimal.ZERO;
        // 注文配送先商品情報検索
        BssOrderDeliveryItemEntity bssOrderDeliveryItemQuery = new BssOrderDeliveryItemEntity();
        bssOrderDeliveryItemQuery.setOrderId(bssOrderInfoVo.getOrderId());
        bssOrderDeliveryItemQuery.setFarmerId(farmerId);
        List<BssOrderDeliveryItemEntity> bssOrderDeliveryItemEntityList = bssOrderDeliveryItemBaseService.selectList(bssOrderDeliveryItemQuery);
        List<BssOrderItemVo> itemList = null;
        if(ValueUtils.isNotEmpty(bssOrderDeliveryItemEntityList)){
            itemList =  BeanUtils.copyBeanList(bssOrderDeliveryItemEntityList , BssOrderItemVo.class);
        }

        // 注文配送先情報検索
        BssOrderDeliveryEntity bssOrderDeliveryQuery = new BssOrderDeliveryEntity();
        bssOrderDeliveryQuery.setOrderId(bssOrderInfoVo.getOrderId());
        bssOrderDeliveryQuery.setFarmerId(farmerId);
        List<BssOrderDeliveryEntity> bssOrderDeliveryEntityList = bssOrderDeliveryBaseService.selectList(bssOrderDeliveryQuery);
        for(BssOrderDeliveryEntity deliveryEntity : bssOrderDeliveryEntityList){
            if(ValueUtils.isNotEmpty(deliveryEntity.getShippingFee())){
                totalCarriage =  totalCarriage.add(deliveryEntity.getShippingFee());
            }
        }
        bssOrderInfoVo.setTotalCarriage(totalCarriage);
        if(ValueUtils.isNotEmpty(bssOrderDeliveryEntityList)){
           List<BssOrderDeliveryVo> deliveryList = BeanUtils.copyBeanList(bssOrderDeliveryEntityList , BssOrderDeliveryVo.class);
           if(ValueUtils.isNotEmpty(deliveryList) && ValueUtils.isNotEmpty(itemList)){
               for(BssOrderDeliveryVo deliveryVo : deliveryList){
                   deliveryVo.setItemList(itemList.stream().filter(m -> ValueUtils.isEqual(deliveryVo.getId(), m.getDeliveryId())).toList());
                   if(ValueUtils.isNotEmpty(deliveryVo.getItemList())){
                       deliveryVo.setItemTotalTax(deliveryVo.getItemList().stream().map(BssOrderItemVo::getSubtotalTax).reduce(BigDecimal::add).get());
                   }
                   deliveryVo.setItemTotalTax(NumberUtils.addBigDecimal(deliveryVo.getItemTotalTax(),deliveryVo.getShippingFee()));
               }
           }
            bssOrderInfoVo.setDeliveryList(deliveryList);
        }

        if(ValueUtils.isNotEmpty(itemList)){
            // 受注商品の合計
            List<BssOrderItemVo> orderItemTotalVoLst = new ArrayList<>(
                    itemList.stream()
                            .collect(Collectors.toMap(
                                    BssOrderItemVo::getSysItemCode, // グループ化キー: 製品コード
                                    vo -> {
                                        // 値マッパー: 各商品コードの最初のサマリー オブジェクトを作成します。
                                        BssOrderItemVo totalVo = new BssOrderItemVo();
                                        totalVo.setOrderId(vo.getOrderId());
                                        totalVo.setFarmerId(vo.getFarmerId());
                                        totalVo.setItemCd(vo.getItemCd());
                                        totalVo.setSysItemCode(vo.getSysItemCode());
                                        totalVo.setItemName(vo.getItemName());
                                        totalVo.setQty(vo.getQty()); // 初期数量
                                        totalVo.setSubtotal(vo.getSubtotal()); // 初期小計
                                        totalVo.setSubtotalTax(vo.getSubtotalTax()); // 税引き後の初期小計
                                        totalVo.setItemPriceTax(vo.getItemPriceTax());
                                        return totalVo;
                                    },
                                    (vo1, vo2) -> {
                                        // 2. 結合: 同じ商品コードがある場合、数量と小計を結合します。
                                        vo1.setQty(vo1.getQty().add(vo2.getQty()));
                                        vo1.setSubtotal(NumberUtils.addBigDecimal(vo1.getSubtotal(), vo2.getSubtotal()));
                                        vo1.setSubtotalTax(NumberUtils.addBigDecimal(vo1.getSubtotalTax(), vo2.getSubtotalTax()));
                                        return vo1;
                                    }
                            ))
                            .values() // マップ内のすべてのBssOrderItemTotalVoオブジェクトを取得します。
            );
            bssOrderInfoVo.setItemList(orderItemTotalVoLst);
        }
        return bssOrderInfoVo;
    }

    /**
     * 顧客購入履歴
     * @param customerId 顧客ID
     * @return
     */
    @Override
    public List<BssOrderItemCustomerVo> selectOrderItemListByCustomerId(String customerId) {
        return bssOrderMapper.selectOrderItemListByCustomerId(SecurityUtils.getLoginUser().getUsername(), customerId);
    }

    private void mergeOrders(List<BssOrderListVo> orders) {
        Map<String, Boolean> seenKeys = new LinkedHashMap<>();

        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        orders.stream()
                .map(order -> {
                    String key = order.getOrderId()+ "|" + order.getOrderTime();

                    if (!seenKeys.containsKey(key)) {
                        seenKeys.put(key, true);
                        return order;
                    } else {
                        order.setOrderId(null);
                        order.setOrderTime(null);
                        return  order;
                    }
                })
                .collect(Collectors.toList());
    }
}
