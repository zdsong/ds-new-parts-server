package jp.newpart.externalwarehouse.sys.mapper;

import java.util.List;

import jp.newpart.common.core.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

/**
 * メニュー表 データ層
 *
 * @author nextosd
 */
public interface SysMenuMapper {

    /**
     * システムメニューリストを查詢
     *
     * @param menu メニュー情報
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * ユーザーの全ての権限を取得
     *
     * @return 権限リスト
     */
    public List<String> selectMenuPerms();

    /**
     * ユーザーのシステムメニューリストを查詢
     *
     * @param menu メニュー情報
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * ロールIDで権限を查詢
     *
     * @param roleId ロールID
     * @return 権限リスト
     */
    public List<String> selectMenuPermsByRoleId(String roleId);

    /**
     * ユーザーIDで権限を查詢
     *
     * @param userId ユーザーID
     * @return 権限リスト
     */
    public List<String> selectMenuPermsByUserId(String userId);

    /**
     * ユーザーIDでメニューツリーを查詢
     *
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * ユーザーIDでメニューツリーを查詢
     *
     * @param userId ユーザーID
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuTreeByUserId(String userId);

    /**
     * ロールIDでメニューツリー情報を查詢
     *
     * @param roleId            ロールID
     * @param menuCheckStrictly メニューツリー選択項目が関連表示されるかどうか
     * @return 選択されたメニューリスト
     */
    public List<String> selectMenuListByRoleId(@Param("roleId") String roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * メニューIDで情報を查詢
     *
     * @param menuId メニューID
     * @return メニュー情報
     */
    public SysMenu selectMenuById(String menuId);

    /**
     * メニューに子ノードが存在するかどうかを確認
     *
     * @param menuId メニューID
     * @return 結果
     */
    public int hasChildByMenuId(String menuId);

    /**
     * メニュー情報を追加
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public int insertMenu(SysMenu menu);

    /**
     * メニュー情報を更新
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public int updateMenu(SysMenu menu);

    /**
     * メニュー管理情報を削除
     *
     * @param menuId メニューID
     * @return 結果
     */
    public int deleteMenuById(String menuId);

    /**
     * メニュー名称の一意性を検証
     *
     * @param menuName メニュー名称
     * @param parentId 親メニューID
     * @return 結果
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") String parentId);
}
