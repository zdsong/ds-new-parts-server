package jp.newpart.externalwarehouse.biz.sys.service;

import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.externalwarehouse.biz.sys.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * ロールビジネス処理
 * 
 * @author nextosd
 */
public interface ISysRoleService
{
    /**
     * 条件付きページングによるロールデータを取得する
     * 
     * @param role ロール情報
     * @return ロールデータリスト情報
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * ユーザーIDにロールリスト情報を取得する
     * 
     * @param userId ユーザーID
     * @return ロールリスト
     */
    public List<SysRole> selectRolesByUserId(String userId);

    /**
     * ユーザーIDにロール権限リストを取得する
     * 
     * @param userId ユーザーID
     * @return 権限リスト
     */
    public Set<String> selectRolePermissionByUserId(String userId);

    /**
     * すべてロールを取得する
     * 
     * @return ロールリスト
     */
    public List<SysRole> selectRoleAll();

    /**
     * ユーザーIDからロール選択ボックスのリストを取得する
     * 
     * @param userId ユーザーID
     * @return ロールリスト
     */
    public List<String> selectRoleListByUserId(String userId);

    /**
     * ロールIDによるロールを取得する
     * 
     * @param roleId ロールID
     * @return ロール情報
     */
    public SysRole selectRoleById(String roleId);

    /**
     * ロール名が一意であることを検証する
     * 
     * @param role ロール情報
     * @return
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * ロール権限が一意であるかどうかを検証する
     * 
     * @param role ロール情報
     * @return
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * ロールが操作を許可するかどうかを検証する
     * 
     * @param role ロール情報
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * ロールにデータ権限があるかどうかを検証する
     * 
     * @param roleIds ロールID
     */
    public void checkRoleDataScope(String... roleIds);

    /**
     * ロールIDによるロール使用量を取得する
     * 
     * @param roleId ロールID
     * @return
     */
    public int countUserRoleByRoleId(String roleId);

    /**
     * ロール情報を新規する
     * 
     * @param role ロール情報
     * @return
     */
    public int insertRole(SysRole role);

    /**
     * ロール情報を変更する
     * 
     * @param role ロール情報
     * @return
     */
    public int updateRole(SysRole role);

    /**
     * ロールステータスを変更する
     * 
     * @param role ロール情報
     * @return
     */
    public int updateRoleStatus(SysRole role);

    /**
     * ロールIDによるロールを削除する
     * 
     * @param roleId ロールID
     * @return
     */
    public int deleteRoleById(String roleId);

    /**
     * ロール情報の一括削除
     * 
     * @param roleIds ロールID
     * @return
     */
    public int deleteRoleByIds(String[] roleIds);

    /**
     * ユーザーロールの権限解除
     * 
     * @param userRole ユーザーとロールの関連情報
     * @return
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * ユーザーロールの一括認可解除
     * 
     * @param roleId ロールID
     * @param userIds ユーザーID
     * @return
     */
    public int deleteAuthUsers(String roleId, String[] userIds);

    /**
     *  ユーザーロールの一括設定
     * 
     * @param roleId ロールID
     * @param userIds ユーザーID
     * @return
     */
    public int insertAuthUsers(String roleId, String[] userIds);

    /**
     * 役割IDを介して役割を削除する
     *
     * @param sysRole ロール対象
     * @return 結果
     */
    public int deleteLogicRoleByIdAndVersion(SysRole sysRole);

    /**
     * ロール情報をキャッシュする
     * @param roleId
     */
    public void setRoleInfoToCache(String roleId);
}
