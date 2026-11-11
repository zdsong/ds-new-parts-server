package jp.newpart.web.controller.monitor;

import jp.newpart.common.annotation.Anonymous;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysJobAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 定期タスク管理コントローラー
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/monitor/job")
public class SysJobAdminController extends BaseController
{
    @Autowired
    private SysJobAdminMapper jobMapper;

    /**
     * タスク一覧を取得する
     *
     * @param jobId タスク番号
     * @param jobName タスク名
     * @param status タスク状態
     * @return タスク一覧
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String jobId,
                              @RequestParam(required = false) String jobName,
                              @RequestParam(required = false) String status)
    {
        startPage();
        return getDataTable(jobMapper.selectJobList(jobId, jobName, status));
    }

    /**
     * タスク詳細を取得する
     *
     * @param jobId タスク番号
     * @return タスク情報
     */
    @Anonymous
    @GetMapping("/{jobId}")
    public AjaxResult getInfo(@PathVariable String jobId)
    {
        return AjaxResult.success(jobMapper.selectJobById(jobId));
    }

    /**
     * タスクを追加する
     *
     * @param job タスク情報
     * @return 処理結果
     */
    @Anonymous
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> job)
    {
        job.put("jobId", IdUtils.nextIdStr());
        if (job.get("status") == null || "".equals(job.get("status")))
        {
            job.put("status", "0");
        }
        return toAjax(jobMapper.insertJob(job));
    }

    /**
     * タスクを更新する
     *
     * @param job タスク情報
     * @return 処理結果
     */
    @Anonymous
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String, Object> job)
    {
        return toAjax(jobMapper.updateJob(job));
    }

    /**
     * タスク状態を変更する
     *
     * @param job タスク情報
     * @return 処理結果
     */
    @Anonymous
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody Map<String, Object> job)
    {
        return toAjax(jobMapper.updateJobStatus(String.valueOf(job.get("jobId")),
                String.valueOf(job.get("status"))));
    }

    /**
     * タスクを即時実行する
     *
     * @return 処理結果
     */
    @Anonymous
    @PutMapping("/run")
    public AjaxResult run()
    {
        return AjaxResult.success();
    }

    /**
     * タスクを削除する
     *
     * @param jobIds 削除対象のタスク番号
     * @return 処理結果
     */
    @Anonymous
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable String[] jobIds)
    {
        return toAjax(jobMapper.deleteJobByIds(jobIds));
    }
}
