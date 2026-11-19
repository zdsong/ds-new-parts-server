package jp.nasta.pms.quartz.mapper;

import jp.nasta.pms.common.core.domain.entity.SysUser;

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
