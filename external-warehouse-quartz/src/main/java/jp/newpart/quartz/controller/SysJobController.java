package jp.newpart.quartz.controller;

import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Log;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.exception.job.TaskException;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.poi.ExcelUtil;
import jp.newpart.quartz.domain.SysJob;
import jp.newpart.quartz.service.ISysJobService;
import jp.newpart.quartz.util.CronUtils;
import jp.newpart.quartz.util.ScheduleUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * スケジュールタスク情報操作処理
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/monitor/job")
public class SysJobController extends BaseController {
    @Autowired
    private ISysJobService jobService;

    /**
     * 定時タスクリストを查詢
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob) {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * 定時タスクリストをエクスポート
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob) {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<>(SysJob.class);
        util.exportExcel(response, list, "定時タスク");
    }

    /**
     * 定時タスク詳細情報を取得
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") String jobId) {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * 新規定時タスクを追加
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、Cron表現が正しくありません");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、対象文字列に'rmi'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、対象文字列に'ldap(s)'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、対象文字列に'http(s)'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、対象文字列に違法な文字が含まれています");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("新規タスク'" + job.getJobName() + "'追加失敗、対象文字列がホワイトリストに含まれていません");
        }
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }

    /**
     * 定時タスクを編集
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("タスク'" + job.getJobName() + "'編集失敗、Cron表現が正しくありません");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("タスク'" + job.getJobName() + "'編集失敗、対象文字列に'rmi'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return error("タスク'" + job.getJobName() + "'編集失敗、対象文字列に'ldap(s)'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return error("タスク'" + job.getJobName() + "'編集失敗、対象文字列に'http(s)'呼び出しは許可されていません");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("タスク'" + job.getJobName() + "'編集失敗、対象文字列に違法な文字が含まれています");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("タスク'" + job.getJobName() + "'編集失敗、対象文字列がホワイトリストに含まれていません");
        }
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }

    /**
     * 定時タスクの状態を変更
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 定時タスクをすぐに実行
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException {
        boolean result = jobService.run(job);
        return result ? success() : error("タスクが存在しないか、有効期限が切れています！");
    }

    /**
     * 定時タスクを削除
     */
    @PreAuthorize("@ss.hasPermi('monitor:job')")
    @Log(title = "定時タスク", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable String[] jobIds) throws SchedulerException, TaskException {
        jobService.deleteJobByIds(jobIds);
        return success();
    }
}
