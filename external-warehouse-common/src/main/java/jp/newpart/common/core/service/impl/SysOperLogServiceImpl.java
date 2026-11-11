package jp.newpart.common.core.service.impl;

import jp.newpart.common.core.domain.entity.SysOperLog;
import jp.newpart.common.core.service.ISysOperLogService;
import jp.newpart.common.mapper.SysOperLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * オペレーションログサービス層処理
 * 
 * @author nextosd
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 操作ログの追加
     * 
     * @param operLog 操作ログ
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * クエリーシステムの操作ログコレクション
     * 
     * @param operLog 操作ログ
     * @return
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * システム操作ログの一括削除
     * 
     * @param operIds Id List
     * @return
     */
    @Override
    public int deleteOperLogByIds(String[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * クエリー操作ログの詳細
     * 
     * @param operId ID
     * @return
     */
    @Override
    public SysOperLog selectOperLogById(String operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * アクションログを空にする
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
