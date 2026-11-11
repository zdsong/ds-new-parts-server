package jp.newpart.web.controller.monitor;

import jp.newpart.common.annotation.Anonymous;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysJobAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定期タスクログ管理コントローラー
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/monitor/jobLog")
public class SysJobLogAdminController extends BaseController
{
    @Autowired
    private SysJobAdminMapper jobLogMapper;

    /**
     * タスクログ一覧を取得する
     *
     * @param jobName タスク名
     * @param jobGroup タスクグループ
     * @param status 状態
     * @return タスクログ一覧
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String jobName,
                              @RequestParam(required = false) String jobGroup,
                              @RequestParam(required = false) String status)
    {
        startPage();
        return getDataTable(jobLogMapper.selectJobLogList(jobName, jobGroup, status));
    }

    /**
     * タスクログを削除する
     *
     * @param jobLogIds 削除対象のログ番号
     * @return 処理結果
     */
    @Anonymous
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable String[] jobLogIds)
    {
        return toAjax(jobLogMapper.deleteJobLogByIds(jobLogIds));
    }

    /**
     * タスクログを全削除する
     *
     * @return 処理結果
     */
    @Anonymous
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        jobLogMapper.cleanJobLog();
        return AjaxResult.success();
    }
}
