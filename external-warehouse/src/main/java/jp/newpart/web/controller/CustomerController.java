package jp.newpart.web.controller;


import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.MastCustomerSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastCustomerVo;
import jp.newpart.externalwarehouse.biz.service.IMastCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 顧客マスタController
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController extends BaseController {

    @Autowired
    private IMastCustomerService mastCustomerService;

    /**
     * 顧客マスタのリスト
     */
    @GetMapping("/list")
    public TableDataInfo list(MastCustomerSearchBo mastCustomerSearchBo) {
        startPage();
        List<MastCustomerVo> list = mastCustomerService.selectCustomerList(mastCustomerSearchBo);
        return getDataTable(list);
    }

    /**
     * 顧客マスタの詳細情報
     */
    @GetMapping(value = "/info/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") String customerId) {
        return AjaxResult.success(mastCustomerService.selectCustomerByCustomerId(customerId));

    }

    /**
     * 顧客マスタの詳細情報
     */
    @GetMapping(value = "/info/byId/{id}")
    public AjaxResult getInfoByCustomerId(@PathVariable("id") String id) {
        return AjaxResult.success(mastCustomerService.selectCustomerById(id));
    }

    /**
     * インポート
     *
     * @param file アップロードするファイル
     * @return アップロード結果
     */
    @Log(title = "新産直くん 顧客マスタ CSVインポート", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult importCsvData(@RequestParam("file") MultipartFile file) {
        List<ImportFileErrorVo> errorList = mastCustomerService.importCsvData(file);

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
