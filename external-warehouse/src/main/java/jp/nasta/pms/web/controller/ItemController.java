package jp.nasta.pms.web.controller;


import jp.nasta.pms.common.annotation.Log;
import jp.nasta.pms.common.core.controller.BaseController;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.common.core.domain.ImportFileErrorVo;
import jp.nasta.pms.common.core.page.TableDataInfo;
import jp.nasta.pms.common.enums.BusinessType;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.nasta.pms.externalwarehouse.biz.service.IMastItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品マスタ Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController extends BaseController {

    @Autowired
    private IMastItemService mastItemService;

    /**
     * 商品マスタのリスト
     */
    @GetMapping("/list")
    public TableDataInfo list(MastItemSearchBo mastItemSearchBo) {
        startPage();
        List<MastItemVo> list = mastItemService.selectList(mastItemSearchBo);
        return getDataTable(list);
    }

    /**
     * 品目マスタの詳細情報
     */
    @GetMapping(value = "/info/{itemCode}")
    public AjaxResult getInfo(@PathVariable("itemCode") String itemCode) {
        return AjaxResult.success(mastItemService.selectByItemCode(itemCode));
    }

    /**
     * インポート
     *
     * @param file アップロードするファイル
     * @return アップロード結果
     */
    @Log(title = "品目マスタ CSVインポート", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult importCsvData(@RequestParam("file") MultipartFile file) {
        List<ImportFileErrorVo> errorList = mastItemService.importCsvData(file);

        if (!CollectionUtils.isEmpty(errorList)) {
            // I0004=取込に失敗しました。
            AjaxResult ajax = AjaxResult.success(MessageUtils.message("I0004"), "error");
            ajax.put("errorList", errorList);
            return ajax;
        }

        // I0003=取込に成功しました。
        return AjaxResult.success(MessageUtils.message("I0003"));
    }
}
