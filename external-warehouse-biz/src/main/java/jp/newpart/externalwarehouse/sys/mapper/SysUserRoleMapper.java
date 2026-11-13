package jp.newpart.externalwarehouse.sys.mapper;

import java.util.List;

import jp.newpart.externalwarehouse.sys.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

/**
 * ユーザーと役割の関連テーブル データ層
 *
 * @author nextosd
 */
public interface SysUserRoleMapper {

    /**
     * ユーザーIDでユーザーと役割の関連を削除
     *
     * @param userId ユーザーID
     * @return 結果
     */
    public int deleteUserRoleByUserId(String userId);

    /**
     * ユーザーと役割の関連を一括削除
     *
     * @param ids 削除するデータID
     * @return 結果
     */
    public int deleteUserRole(String[] ids);

    /**
     * 役割IDで役割の使用数を查詢
     *
     * @param roleId 役割ID
     * @return 結果
     */
    public int countUserRoleByRoleId(String roleId);

    /**
     * ユーザー役割情報を一括追加
     *
     * @param userRoleList ユーザー役割リスト
     * @return 結果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * ユーザーと役割の関連情報を削除
     *
     * @param userRole ユーザーと役割の関連情報
     * @return 結果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * ユーザー役割の権限を一括キャンセル
     *
     * @param roleId  役割ID
     * @param userIds 削除するユーザーデータID
     * @return 結果
     */
    public int deleteUserRoleInfos(@Param("roleId") String roleId, @Param("userIds") String[] userIds);
}
