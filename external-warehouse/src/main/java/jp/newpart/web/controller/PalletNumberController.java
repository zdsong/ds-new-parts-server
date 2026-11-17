package jp.newpart.web.controller;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.newpart.common.biz.domain.entity.BssPalletNumberEntity;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.BssPalletNumberBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssPalletNumberSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssPalletNumberVo;
import jp.newpart.externalwarehouse.biz.service.IBssPalletNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * パレット番号 Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/pallet")
public class PalletNumberController extends BaseController {

    @Autowired
    private IBssPalletNumberService bssPalletNumberService;

    /**
     * パレット番号のリスト
     */
    @GetMapping("/list")
    public TableDataInfo list(BssPalletNumberSearchBo searchBo) {
        startPage();
        List<BssPalletNumberVo> list = bssPalletNumberService.selectList(searchBo);
        return getDataTable(list);
    }

    /**
     * パレット情報の詳細情報
     */
    @GetMapping(value = "/{palletNo}")
    public AjaxResult getInfo(@PathVariable("palletNo") String palletNo) {
        BssPalletNumberVo pallet = bssPalletNumberService.selectByPalletNo(palletNo);
        if (pallet == null) {
            // E0016=パレット番号は存在しません。
            return error(MessageUtils.message("E0016", MessageUtils.message("label.palletNo")));
        }
        return success(pallet);
    }

    /**
     * パレット番号を発番して登録
     */
    @Log(title = "パレット番号発番", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BssPalletNumberBo bo) {
        AjaxResult validateResult = validatePallet(bo);
        if (validateResult != null) {
            return validateResult;
        }

        int rows = bssPalletNumberService.insertPallet(bo);
        if (rows == 0) {
            return error(MessageUtils.message("I0002"));
        }

        // 採番したパレット番号を呼び出し元へ返す
        AjaxResult ajax = success(MessageUtils.message("I0001"));
        ajax.put("palletNo", bo.getPalletNo());
        return ajax;
    }

    /**
     * パレット情報を更新
     */
    @Log(title = "パレット情報更新", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BssPalletNumberBo bo) {
        if (StringUtils.isEmpty(bo.getPalletNo()) || bo.getVersion() == null) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }

        AjaxResult validateResult = validatePallet(bo);
        if (validateResult != null) {
            return validateResult;
        }

        int rows = bssPalletNumberService.updatePallet(bo);
        if (rows == 0) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }
        return success(MessageUtils.message("I0001"));
    }

    /**
     * パレット番号表を印刷済みにする（印刷フラグを 1 に更新）
     */
    @Log(title = "パレット番号表印刷", businessType = BusinessType.UPDATE)
    @PutMapping("/{palletNo}/printed")
    public AjaxResult markPrinted(@PathVariable("palletNo") String palletNo) {
        if (StringUtils.isEmpty(palletNo)) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.palletNo")));
        }

        int rows = bssPalletNumberService.updatePrintedFlg(palletNo);
        if (rows == 0) {
            // パレット番号は存在しません。
            return error(MessageUtils.message("E0016", MessageUtils.message("label.palletNo")));
        }
        return success(MessageUtils.message("I0001"));
    }

    /**
     * パレット情報を論理削除
     */
    @Log(title = "パレット情報削除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{palletNo}")
    public AjaxResult remove(@PathVariable("palletNo") String palletNo) {
        if (StringUtils.isEmpty(palletNo)) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.palletNo")));
        }

        BssPalletNumberEntity entity = new BssPalletNumberEntity();
        entity.setPalletNo(palletNo);

        int rows = bssPalletNumberService.deletePallet(entity);
        if (rows == 0) {
            // パレット番号は存在しません。
            return error(MessageUtils.message("E0016", MessageUtils.message("label.palletNo")));
        }
        return success(MessageUtils.message("I0001"));
    }

    /**
     * 入力チェックを行う
     *
     * @param bo パレット番号
     * @return エラーがない場合は null
     */
    private AjaxResult validatePallet(BssPalletNumberBo bo) {
        if (StringUtils.isEmpty(bo.getClassification())) {
            // 区分は必須選択項目です。
            return error(MessageUtils.message("E0023", MessageUtils.message("label.classification")));
        }
        if (StringUtils.isEmpty(bo.getWhCd())) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.warehouse")));
        }
        if (StringUtils.isEmpty(bo.getLocCd())) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.locationCd")));
        }
        if (CollectionUtils.isEmpty(bo.getDetails())) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.loadedItem")));
        }

        for (BssPalletNumberDetailEntity detail : bo.getDetails()) {
            if (StringUtils.isEmpty(detail.getItemCode())) {
                return error(MessageUtils.message("E0003", MessageUtils.message("label.partItemCode")));
            }
            if (detail.getStorageQty() == null) {
                return error(MessageUtils.message("E0003", MessageUtils.message("label.storageQty")));
            }
        }

        // 前後の空白を除去して保存する
        bo.setClassification(bo.getClassification().trim());
        bo.setWhCd(bo.getWhCd().trim());
        bo.setLocCd(bo.getLocCd().trim());
        if (bo.getMemo1() != null) {
            bo.setMemo1(bo.getMemo1().trim());
        }
        if (bo.getStoragePeriod() != null) {
            bo.setStoragePeriod(bo.getStoragePeriod().trim());
        }
        for (BssPalletNumberDetailEntity detail : bo.getDetails()) {
            detail.setItemCode(detail.getItemCode().trim());
        }
        return null;
    }
}
