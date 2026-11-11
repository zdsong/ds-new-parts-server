package jp.newpart.externalwarehouse.biz.sys.mapper;

import jp.newpart.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * 役割テーブル データ層
 *
 * @author nextosd
 */
public interface SysRoleMapper {
    /**
     * 条件に応じてページ分割して役割データを查詢
     *
     * @param role 役割情報
     * @return 役割データ集合情報
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * ユーザーIDで役割を查詢
     *
     * @param userId ユーザーID
     * @return 役割リスト
     */
    public List<SysRole> selectRolePermissionByUserId(String userId);

    /**
     * 全ての役割を查詢
     *
     * @return 役割リスト
     */
    public List<SysRole> selectRoleAll();

    /**
     * ユーザーIDで役割選択ボックスリストを取得
     *
     * @param userId ユーザーID
     * @return 選択された役割IDリスト
     */
    public List<String> selectRoleListByUserId(String userId);

    /**
     * 役割IDで役割を查詢
     *
     * @param roleId 役割ID
     * @return 役割オブジェクト情報
     */
    public SysRole selectRoleById(String roleId);

    /**
     * 役割IDで役割を查詢
     *
     * @param roleId 役割ID
     * @return 役割オブジェクト情報
     */
    public SysRole selectRoleByIdAndDelFlag(String roleId);

    /**
     * ユーザーネームで役割を查詢
     *
     * @param userName ユーザーネーム
     * @return 役割リスト
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 役割名称の一意性を検証
     *
     * @param roleName 役割名称
     * @return 役割情報
     */
    public Integer checkRoleNameUnique(String roleId, String roleName);

    /**
     * 役割権限の一意性を検証
     *
     * @param roleKey 役割権限
     * @return 役割情報
     */
    public Integer checkRoleKeyUnique(String roleId, String roleKey);

    /**
     * 役割情報を更新
     *
     * @param role 役割情報
     * @return 結果
     */
    public int updateRole(SysRole role);

    /**
     * 役割情報を追加
     *
     * @param role 役割情報
     * @return 結果
     */
    public int insertRole(SysRole role);

    /**
     * 役割IDで役割を削除
     *
     * @param roleId 役割ID
     * @return 結果
     */
    public int deleteRoleById(String roleId);

    /**
     * 役割情報を一括削除
     *
     * @param roleIds 削除する役割ID
     * @return 結果
     */
    public int deleteRoleByIds(String[] roleIds);

    /**
     * 役割IDで役割を削除
     *
     * @param role 役割対象
     * @return 結果
     */
    public int deleteLogicRoleByIdAndVersion(SysRole role);
}
