package jp.newpart.quartz.mapper;

import java.util.List;
import jp.newpart.quartz.domain.SysJob;

/**
 * スケジュールタスク情報
 * 
 * @author nextosd
 */
public interface SysJobMapper
{
    /**
     * クエリスケジューリングタスクログコレクション
     * 
     * @param job スケジュール情報
     * @return
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * すべてのスケジュールタスクの取得
     * 
     * @return
     */
    public List<SysJob> selectJobAll();

    /**
     * スケジューリングIDによるスケジューリングタスク情報の照会
     * 
     * @param jobId
     * @return
     */
    public SysJob selectJobById(String jobId);

    /**
     * スケジュールIDによるスケジュールタスク情報の削除
     * 
     * @param jobId
     * @return
     */
    public int deleteJobById(String jobId);

    /**
     * 一括削除スケジュールタスク情報
     * 
     * @param ids
     * @return
     */
    public int deleteJobByIds(String[] ids);

    /**
     * スケジュールタスク情報の変更
     * 
     * @param job
     * @return
     */
    public int updateJob(SysJob job);

    /**
     * 新規スケジュールタスク情報
     * 
     * @param job スケジュールタスク情報
     * @return
     */
    public int insertJob(SysJob job);
}
