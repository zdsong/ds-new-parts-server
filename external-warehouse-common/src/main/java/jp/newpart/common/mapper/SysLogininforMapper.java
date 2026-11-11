package jp.newpart.common.mapper;

import jp.newpart.common.core.domain.entity.SysLogininfor;

import java.util.List;

/**
 * システムアクセスログ状況情報
 *
 * @author nextosd
 */
public interface SysLogininforMapper
{
    /**
     * システムログインログ追加
     *
     * @param logininfor ログ・オブジェクトへのアクセス
     */
    public void insertLogininfor(SysLogininfor logininfor);

    /**
     * クエリシステムログインログコレクション
     *
     * @param logininfor ログ・オブジェクトへのアクセス
     * @return
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * システムログインログの一括削除
     *
     * @param infoIds ID List
     * @return
     */
    public int deleteLogininforByIds(String[] infoIds);

    /**
     * すべてのシステムログインログを削除
     */
    public int cleanLogininfor();
}
