package jp.nasta.pms.web.controller;

import jp.nasta.pms.common.core.controller.BaseController;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssInventorySearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssInventoryLocationVo;
import jp.nasta.pms.externalwarehouse.biz.service.IBssInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在庫情報 Controller
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController extends BaseController {

    @Autowired
    private IBssInventoryService bssInventoryService;

    /**
     * 在庫一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫一覧
     */
    @GetMapping("/list")
    public AjaxResult list(BssInventorySearchBo searchBo) {
        return success(bssInventoryService.selectStockList(searchBo));
    }

    /**
     * 品目の在庫詳細を取得する
     *
     * @param itemCode 品目コード
     * @return 在庫詳細
     */
    @GetMapping("/item/{itemCode}")
    public AjaxResult item(@PathVariable("itemCode") String itemCode) {
        if (StringUtils.isEmpty(itemCode)) {
            return error(MessageUtils.message("E0003", "品目コード"));
        }
        BssInventoryItemVo detail = bssInventoryService.selectItemDetail(itemCode);
        if (detail == null) {
            return error(MessageUtils.message("E0016", "在庫情報"));
        }
        return success(detail);
    }

    /**
     * ロケ番の在庫詳細を取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return 在庫詳細
     */
    @GetMapping("/location")
    public AjaxResult location(@RequestParam("whCd") String whCd, @RequestParam("locCd") String locCd) {
        if (StringUtils.isEmpty(whCd) || StringUtils.isEmpty(locCd)) {
            return error(MessageUtils.message("E0003", "ロケ番"));
        }
        BssInventoryLocationVo detail = bssInventoryService.selectLocationDetail(whCd, locCd);
        if (detail == null) {
            return error(MessageUtils.message("E0016", "在庫情報"));
        }
        return success(detail);
    }
}
