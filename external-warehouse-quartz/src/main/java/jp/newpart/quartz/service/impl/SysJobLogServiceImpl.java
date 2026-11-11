package jp.newpart.quartz.service.impl;

import jp.newpart.quartz.domain.SysJobLog;
import jp.newpart.quartz.domain.SysJobLogQueryBo;
import jp.newpart.quartz.mapper.SysJobLogMapper;
import jp.newpart.quartz.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定時タスクスケジューリングログ情報 サービス層
 *
 * @author nextosd
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService {

    @Autowired
    private SysJobLogMapper jobLogMapper;

    /**
     * quartzスケジューラーログの計画タスクリストを取得する
     *
     * @param jobLog スケジュールログ情報
     * @return スケジュールタスクログ集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLogQueryBo jobLog) {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    /**
     * スケジュールタスクログIDでスケジュール情報を取得する
     *
     * @param jobLogId スケジュールタスクログID
     * @return スケジュールタスクログオブジェクト情報
     */
    @Override
    public SysJobLog selectJobLogById(String jobLogId) {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * 新規タスクログを追加する
     *
     * @param jobLog スケジュールログ情報
     */
    @Override
    public void addJobLog(SysJobLog jobLog) {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * 複数のスケジュールログ情報を一括削除する
     *
     * @param logIds 削除が必要なデータID
     * @return 結果
     */
    @Override
    public int deleteJobLogByIds(String[] logIds) {
        return jobLogMapper.deleteJobLogByIds(logIds);
    }

    /**
     * タスクログを削除する
     *
     * @param jobId スケジュールログID
     */
    @Override
    public int deleteJobLogById(String jobId) {
        return jobLogMapper.deleteJobLogById(jobId);
    }

    /**
     * タスクログをクリアする
     */
    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
}
