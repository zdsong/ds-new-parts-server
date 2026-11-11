package jp.newpart.web.controller;


import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.newpart.externalwarehouse.biz.service.IMastItemService;
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
     * 商品マスタの詳細情報
     */
    @GetMapping(value = "/info/{itemId}")
    public AjaxResult getInfo(@PathVariable("itemId") String itemId) {
        return AjaxResult.success(mastItemService.selectByItemId(itemId));
    }

    /**
     * インポート
     *
     * @param file アップロードするファイル
     * @return アップロード結果
     */
    @Log(title = "新産直くん 商品マスタ CSVインポート", businessType = BusinessType.IMPORT)
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
