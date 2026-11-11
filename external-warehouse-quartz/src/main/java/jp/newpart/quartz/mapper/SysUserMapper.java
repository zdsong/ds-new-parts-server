package jp.newpart.quartz.mapper;

import jp.newpart.common.core.domain.entity.SysUser;

/**
 * ユーザーテーブル データ層
 *
 * @author nextosd
 */
public interface SysUserMapper {

    /**
     * ユーザーネームでユーザーを查詢
     *
     * @param userName ユーザーネーム
     * @return ユーザーオブジェクト情報
     */
    public SysUser selectUserByUserName(String userName);

}
