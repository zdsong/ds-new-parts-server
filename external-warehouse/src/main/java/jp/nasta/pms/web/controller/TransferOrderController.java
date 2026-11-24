package jp.nasta.pms.web.controller;

import jp.nasta.pms.common.core.controller.BaseController;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveCompleteBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveRegisterBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveStockSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.service.IBssTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * 移動依頼（出庫）の対象となる在庫明細を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫明細
     */
    @GetMapping("/candidates")
    public AjaxResult candidates(BssTransferCandidateSearchBo searchBo) {
        return success(bssTransferOrderService.selectTransferCandidates(searchBo));
    }

    /**
     * 移動依頼を登録する
     *
     * @param bo 移動依頼
     * @return 移動依頼番号
     */
    @PostMapping("/request")
    public AjaxResult request(@RequestBody BssTransferOrderBo bo) {
        return success(bssTransferOrderService.insertTransferOrder(bo));
    }

    /**
     * 移動依頼詳細を取得する
     *
     * @param requestNo 移動依頼番号
     * @return 移動依頼詳細
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam("requestNo") String requestNo) {
        return success(bssTransferOrderService.selectTransferDetail(requestNo));
    }

    /**
     * 移動依頼を更新する
     *
     * @param bo 移動依頼
     * @return 更新結果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody BssTransferOrderBo bo) {
        bssTransferOrderService.updateTransferOrder(bo);
        return success();
    }

    /**
     * 移動登録を行う（SP402）
     *
     * @param bo 移動登録の内容
     * @return 移動依頼番号と移動後のパレット番号
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody BssMoveRegisterBo bo) {
        return success(bssTransferOrderService.registerMove(bo));
    }

    /**
     * 移動一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動一覧
     */
    @GetMapping("/moves")
    public AjaxResult moves(BssMoveSearchBo searchBo) {
        return success(bssTransferOrderService.selectMoveList(searchBo));
    }

    /**
     * 移動情報詳細を取得する
     *
     * @param transferNo 移動番号
     * @return 移動情報詳細
     */
    @GetMapping("/move/detail")
    public AjaxResult moveDetail(@RequestParam("transferNo") String transferNo) {
        return success(bssTransferOrderService.selectMoveDetail(transferNo));
    }

    /**
     * 移動元の在庫明細を取得する
     *
     * @param searchBo 検索条件
     * @return 在庫明細
     */
    @GetMapping("/stock")
    public AjaxResult stock(BssMoveStockSearchBo searchBo) {
        return success(bssTransferOrderService.selectMoveStockList(searchBo));
    }

    /**
     * 移動先パレットの移動情報を取得する（SP406 読取）
     *
     * @param palletNo パレット番号
     * @return 移動情報
     */
    @GetMapping("/receive/scan")
    public AjaxResult receiveScan(@RequestParam("palletNo") String palletNo) {
        return success(bssTransferOrderService.selectReceiveMove(palletNo));
    }

    /**
     * 移動完了を登録する（SP406）
     *
     * @param bo 移動完了の内容
     * @return 処理結果
     */
    @PostMapping("/complete")
    public AjaxResult complete(@RequestBody BssMoveCompleteBo bo) {
        bssTransferOrderService.completeMove(bo);
        return success();
    }
}
