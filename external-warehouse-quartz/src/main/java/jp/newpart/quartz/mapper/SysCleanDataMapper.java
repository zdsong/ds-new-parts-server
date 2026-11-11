package jp.newpart.quartz.mapper;

/**
 * 不要なデータのクリーンアップ
 *
 * @author nextosd
 */
public interface SysCleanDataMapper {

    /**
     * オペレーションログ
     *
     * @param prevDate
     * @return
     */
    int deleteSysOperLogByDate(String prevDate);

    /**
     * システムアクセスログ
     *
     * @param prevDate
     * @return
     */
    int deleteSysLogininforByDate(String prevDate);

    /**
     * タスクスケジュールログ
     * @param prevDate
     * @return
     */
    int deleteJobLogByDate(String prevDate);

    /**
     * ユーザー認証情報
     *
     * @param prevDate
     * @return
     */
    int deleteBssUserCertificationByDate(String prevDate);
}
