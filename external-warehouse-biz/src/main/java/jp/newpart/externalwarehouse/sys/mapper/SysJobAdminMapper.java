package jp.newpart.externalwarehouse.sys.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 定期タスク管理用マッパー
 *
 * @author nextosd
 */
@Mapper
public interface SysJobAdminMapper
{
    /**
     * タスク一覧を取得する
     *
     * @param jobId タスク番号
     * @param jobName タスク名
     * @param status タスク状態
     * @return タスク一覧
     */
    @Select("<script>"
            + "select job_id as \"jobId\", job_name as \"jobName\", job_group as \"jobGroup\", "
            + "invoke_target as \"invokeTarget\", cron_expression as \"cronExpression\", "
            + "misfire_policy as \"misfirePolicy\", concurrent, status, "
            + "create_time as \"createTime\", update_time as \"updateTime\", remark "
            + "from sys_job "
            + "<where>"
            + "<if test='jobId != null and jobId != \"\"'>and job_id = #{jobId}</if>"
            + "<if test='jobName != null and jobName != \"\"'>and job_name like '%' || #{jobName} || '%'</if>"
            + "<if test='status != null and status != \"\"'>and status = #{status}</if>"
            + "</where>"
            + "order by job_id"
            + "</script>")
    List<Map<String, Object>> selectJobList(@Param("jobId") String jobId,
                                            @Param("jobName") String jobName,
                                            @Param("status") String status);

    /**
     * タスク番号でタスクを取得する
     *
     * @param jobId タスク番号
     * @return タスク情報
     */
    @Select("select job_id as \"jobId\", job_name as \"jobName\", job_group as \"jobGroup\", "
            + "invoke_target as \"invokeTarget\", cron_expression as \"cronExpression\", "
            + "misfire_policy as \"misfirePolicy\", concurrent, status, "
            + "create_time as \"createTime\", update_time as \"updateTime\", remark "
            + "from sys_job where job_id = #{jobId}")
    Map<String, Object> selectJobById(@Param("jobId") String jobId);

    /**
     * タスクを追加する
     *
     * @param job タスク情報
     * @return 更新件数
     */
    @Insert("insert into sys_job(job_id, job_name, job_group, invoke_target, cron_expression, "
            + "misfire_policy, concurrent, status, create_by, create_time, remark) "
            + "values(#{jobId}, #{jobName}, #{jobGroup}, #{invokeTarget}, #{cronExpression}, "
            + "#{misfirePolicy}, #{concurrent}, #{status}, #{createBy}, now(), #{remark})")
    int insertJob(Map<String, Object> job);

    /**
     * タスクを更新する
     *
     * @param job タスク情報
     * @return 更新件数
     */
    @Update("update sys_job set job_name = #{jobName}, job_group = #{jobGroup}, "
            + "invoke_target = #{invokeTarget}, cron_expression = #{cronExpression}, "
            + "misfire_policy = #{misfirePolicy}, concurrent = #{concurrent}, status = #{status}, "
            + "remark = #{remark}, update_time = now() where job_id = #{jobId}")
    int updateJob(Map<String, Object> job);

    /**
     * タスク状態を更新する
     *
     * @param jobId タスク番号
     * @param status タスク状態
     * @return 更新件数
     */
    @Update("update sys_job set status = #{status}, update_time = now() where job_id = #{jobId}")
    int updateJobStatus(@Param("jobId") String jobId, @Param("status") String status);

    /**
     * タスクを削除する
     *
     * @param jobIds 削除対象のタスク番号
     * @return 更新件数
     */
    @Delete("<script>delete from sys_job where job_id in "
            + "<foreach collection='jobIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>"
            + "</script>")
    int deleteJobByIds(@Param("jobIds") String[] jobIds);

    /**
     * タスクログ一覧を取得する
     *
     * @param jobName タスク名
     * @param jobGroup タスクグループ
     * @param status 状態
     * @return タスクログ一覧
     */
    @Select("<script>"
            + "select job_log_id as \"jobLogId\", job_name as \"jobName\", job_group as \"jobGroup\", "
            + "invoke_target as \"invokeTarget\", job_message as \"jobMessage\", status, "
            + "exception_info as \"exceptionInfo\", create_time as \"createTime\" "
            + "from sys_job_log "
            + "<where>"
            + "<if test='jobName != null and jobName != \"\"'>and job_name like '%' || #{jobName} || '%'</if>"
            + "<if test='jobGroup != null and jobGroup != \"\"'>and job_group = #{jobGroup}</if>"
            + "<if test='status != null and status != \"\"'>and status = #{status}</if>"
            + "</where>"
            + "order by create_time desc"
            + "</script>")
    List<Map<String, Object>> selectJobLogList(@Param("jobName") String jobName,
                                               @Param("jobGroup") String jobGroup,
                                               @Param("status") String status);

    /**
     * タスクログを削除する
     *
     * @param jobLogIds 削除対象のログ番号
     * @return 更新件数
     */
    @Delete("<script>delete from sys_job_log where job_log_id in "
            + "<foreach collection='jobLogIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>"
            + "</script>")
    int deleteJobLogByIds(@Param("jobLogIds") String[] jobLogIds);

    /**
     * タスクログを全削除する
     *
     * @return 更新件数
     */
    @Delete("truncate table sys_job_log")
    int cleanJobLog();
}
