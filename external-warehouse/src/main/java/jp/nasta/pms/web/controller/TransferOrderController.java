package jp.nasta.pms.web.controller;

import jp.nasta.pms.common.core.controller.BaseController;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.service.IBssTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移動依頼 Controller
 */
@RestController
@RequestMapping("/api/transfer")
public class TransferOrderController extends BaseController {

    @Autowired
    private IBssTransferOrderService bssTransferOrderService;

    /**
     * 移動依頼一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動依頼一覧
     */
    @GetMapping("/list")
    public AjaxResult list(BssTransferSearchBo searchBo) {
        return success(bssTransferOrderService.selectTransferList(searchBo));
    }
}
