package jp.newpart.common.mapper;

import jp.newpart.common.core.domain.entity.SysOperLog;

import java.util.List;

/**
 * 操作ログ
 * 
 * @author nextosd
 */
public interface SysOperLogMapper
{
    /**
     * 操作ログの追加
     *
     * @param operLog 操作ログ
     */
    public void insertOperlog(SysOperLog operLog);

    /**
     * クエリーシステムの操作ログコレクション
     *
     * @param operLog 操作ログ
     * @return
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * システム操作ログの一括削除
     *
     * @param operIds Id List
     * @return
     */
    public int deleteOperLogByIds(String[] operIds);

    /**
     * クエリー操作ログの詳細
     *
     * @param operId ID
     * @return
     */
    public SysOperLog selectOperLogById(String operId);

    /**
     * アクションログを空にする
     */
    public void cleanOperLog();
}
