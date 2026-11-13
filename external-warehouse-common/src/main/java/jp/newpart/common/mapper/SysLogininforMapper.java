package jp.newpart.common.mapper;

import jp.newpart.common.core.domain.entity.SysLogininfor;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    /**
     * 指定ユーザーの直近のログイン成功日時を取得する
     *
     * @param userName        ユーザーID
     * @param successMessages ログイン成功と見なすメッセージ一覧（ログアウト等を除外するため）
     * @return 直近のログイン成功日時（記録が無い場合は null）
     */
    public Date selectLastLoginTime(@Param("userName") String userName,
                                    @Param("successMessages") List<String> successMessages);
}
