package jp.newpart.common.core.service;

import jp.newpart.common.core.domain.entity.SysLogininfor;

import java.util.List;

/**
 * システムアクセスログ状況情報サービス層処理
 *
 * @author nextosd
 */
public interface ISysLogininforService
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
    public void cleanLogininfor();
}
