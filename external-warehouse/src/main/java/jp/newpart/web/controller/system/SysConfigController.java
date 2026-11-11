package jp.newpart.web.controller.system;

import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.domain.entity.SysConfig;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
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
 * パラメータ情報操作処理
 * 
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * パラメータ構成リストの取得
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "パラメータ管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "パラメータ管理");
    }

    /**
     * パラメータ番号による詳細の取得
     */
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable String configId)
    {
        return success(configService.selectConfigById(configId));
    }

    /**
     * パラメータ・キー名によるパラメータ値の問合せ
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新しいパラメータ構成
     */
    @Log(title = "パラメータ管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("新規パラメータ'" + config.getConfigName() + "'失敗、パラメータキー名はすでに存在します");
        }
        config.setCreateBy(getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * パラメータ構成の変更
     */
    @Log(title = "パラメータ管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("パラメータを修正する'" + config.getConfigName() + "'失敗、パラメータキー名はすでに存在します");
        }
        config.setUpdateBy(getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * パラメータ構成の削除
     */
    @Log(title = "パラメータ管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable String[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * パラメータキャッシュのリフレッシュ
     */
    @Log(title = "パラメータ管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        configService.resetConfigCache();
        return success();
    }
}
