package jp.newpart.common.core.service.impl;

import jp.newpart.common.core.domain.entity.SysLogininfor;
import jp.newpart.common.core.service.ISysLogininforService;
import jp.newpart.common.mapper.SysLogininforMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * システムアクセスログ状況情報サービス層処理
 * 
 * @author nextosd
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * システムログインログ追加
     * 
     * @param logininfor ログ・オブジェクトへのアクセス
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * クエリシステムログインログコレクション
     * 
     * @param logininfor ログ・オブジェクトへのアクセス
     * @return
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * システムログインログの一括削除
     * 
     * @param infoIds ID List
     * @return
     */
    @Override
    public int deleteLogininforByIds(String[] infoIds)
    {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * すべてのシステムログインログを削除
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
