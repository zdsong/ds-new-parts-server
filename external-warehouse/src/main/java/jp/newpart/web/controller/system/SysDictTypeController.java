package jp.newpart.web.controller.system;

import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.service.ISysDictTypeService;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysDictType;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.poi.ExcelUtil;
import jp.newpart.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * データ辞書情報
 * 
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @Log(title = "辞書タイプ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "辞書タイプ");
    }

    /**
     * 辞書タイプの詳細を取得する
     */
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable String dictId) {
        return success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新規辞書タイプを追加する
     */
    @Log(title = "辞書タイプ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新規辞書'" + dict.getDictName() + "'の追加に失敗しました。辞書タイプが既に存在します");
        }
        dict.setCreateBy(getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 辞書タイプを更新する
     */
    @Log(title = "辞書タイプ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("辞書'" + dict.getDictName() + "'の更新に失敗しました。辞書タイプが既に存在します");
        }
        dict.setUpdateBy(getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 辞書タイプを削除する
     */
    @Log(title = "辞書タイプ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable String[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 辞書キャッシュをリフレッシュする
     */
    @Log(title = "辞書タイプ", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 辞書選択ボックスリストを取得する
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
}