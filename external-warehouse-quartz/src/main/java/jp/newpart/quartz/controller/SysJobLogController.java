package jp.newpart.quartz.controller;

import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.poi.ExcelUtil;
import jp.newpart.quartz.domain.SysJobLog;
import jp.newpart.quartz.domain.SysJobLogQueryBo;
import jp.newpart.quartz.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * スケジュールログ操作処理
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/monitor/jobLog")
public class SysJobLogController extends BaseController {

    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 定時タスクスケジュールログリストを查詢
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLogQueryBo sysJobLog) {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 定時タスクスケジュールログリストをエクスポート
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "タスクスケジュールログ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJobLogQueryBo sysJobLog) {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<>(SysJobLog.class);
        util.exportExcel(response, list, "スケジュールログ");
    }

    /**
     * スケジュール番号に基づいて詳細情報を取得
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable String jobLogId) {
        return success(jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 定時タスクスケジュールログを削除
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスクスケジュールログ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable String[] jobLogIds) {
        return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 定時タスクスケジュールログをクリア
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "スケジュールログ", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        jobLogService.cleanJobLog();
        return success();
    }
}