package jp.newpart.externalwarehouse.sys.mapper;

import java.util.List;

import jp.newpart.externalwarehouse.sys.domain.SysRoleMenu;

/**
 * 役割とメニューの関連テーブル データ層
 *
 * @author nextosd
 */
public interface SysRoleMenuMapper {
    /**
     * メニューの使用数を查詢
     *
     * @param menuId メニューID
     * @return 結果
     */
    public int checkMenuExistRole(String menuId);

    /**
     * 役割IDで役割とメニューの関連を削除
     *
     * @param roleId 役割ID
     * @return 結果
     */
    public int deleteRoleMenuByRoleId(String roleId);

    /**
     * 役割とメニューの関連情報を一括削除
     *
     * @param ids 削除するデータID
     * @return 結果
     */
    public int deleteRoleMenu(String[] ids);

    /**
     * 役割メニュー情報を一括追加
     *
     * @param roleMenuList 役割メニューリスト
     * @return 結果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
