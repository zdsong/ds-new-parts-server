package jp.newpart.web.controller;

import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssOrderSearchBo;
import jp.newpart.externalwarehouse.biz.service.IBssAskService;
import jp.newpart.externalwarehouse.biz.service.IBssOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注文情報Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

    @Autowired
    private IBssOrderService bssOrderService;

    @Autowired
    private IBssAskService bssAskService;

    /**
     * 注文情報のリスト
     */
    @PostMapping("/list")
    public TableDataInfo getList(@RequestBody BssOrderSearchBo orderSearchBo) {
        startPage();
        return getDataTable(bssOrderService.selectList(orderSearchBo));
    }

    /**
     * 注文情報のリスト
     *
     * 注文配送先情報Group
     */
    @PostMapping("/list/delivery")
    public TableDataInfo getListDelivery(@RequestBody BssOrderSearchBo orderSearchBo) {
        startPage();
        return getDataTable(bssOrderService.selectListDeliveryId(orderSearchBo));
    }

    /**
     * 注文詳細情報を取得する
     *
     * @param orderId 注文ID
     */
    @GetMapping("/{orderId}/detail")
    public AjaxResult detail(@PathVariable("orderId") String orderId) {
        return AjaxResult.success(bssOrderService.selectOrderByOrderId(orderId));
    }

    /**
     * 注文番号に紐づくお問い合わせ履歴を表示する
     *
     * @return お問い合わせ履歴リスト
     */
    @GetMapping(value = "/ask/{orderId}/list")
    public AjaxResult getOrderAskList(@PathVariable("orderId") String orderId) {
        return AjaxResult.success(bssAskService.getAskListHistoryByOrderId(orderId));
    }

    /**
     * 顧客購入履歴を取得する
     *
     * @return 顧客購入リスト
     */
    @GetMapping(value = "/list/byCustomerId")
    public TableDataInfo getOrderListByCustomerId(@RequestParam("customerId") String customerId) {
        startPage();
        return getDataTable(bssOrderService.selectOrderItemListByCustomerId(customerId));
    }
}
