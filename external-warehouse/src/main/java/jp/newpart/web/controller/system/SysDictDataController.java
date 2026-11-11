package jp.newpart.web.controller.system;

import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.service.ISysDictDataService;
import jp.newpart.common.biz.service.ISysDictTypeService;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * データ辞書情報
 * 
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "辞書データ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "辞書データ");
    }

    /**
     * クエリー辞書データの詳細
     */
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable String dictCode)
    {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * Get Dict By Type
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * Insert
     */
    @Log(title = "辞書データ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * Update
     */
    @Log(title = "辞書データ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * Delete
     */
    @Log(title = "辞書データ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable String[] dictCodes)
    {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
