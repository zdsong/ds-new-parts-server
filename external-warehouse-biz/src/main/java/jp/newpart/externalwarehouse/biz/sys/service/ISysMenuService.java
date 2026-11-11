package jp.newpart.externalwarehouse.biz.sys.service;

import jp.newpart.common.core.domain.TreeSelect;
import jp.newpart.common.core.domain.entity.SysMenu;
import jp.newpart.externalwarehouse.biz.sys.domain.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * メニュー ビジネス層
 *
 * @author nextosd
 */
public interface ISysMenuService {
    /**
     * ユーザーに基づいてシステムメニューリストを検索する
     *
     * @param userId ユーザーID
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuList(String userId);

    /**
     * ユーザーに基づいてシステムメニューリストを検索する
     *
     * @param menu   メニュー情報
     * @param userId ユーザーID
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuList(SysMenu menu, String userId);

    /**
     * ユーザーIDに基づいて権限を検索する
     *
     * @param userId ユーザーID
     * @return 権限リスト
     */
    public Set<String> selectMenuPermsByUserId(String userId);

    /**
     * ロールIDに基づいて権限を検索する
     *
     * @param roleId ロールID
     * @return 権限リスト
     */
    public Set<String> selectMenuPermsByRoleId(String roleId);

    /**
     * ユーザーIDに基づいてメニューツリー情報を検索する
     *
     * @param userId ユーザーID
     * @return メニューリスト
     */
    public List<SysMenu> selectMenuTreeByUserId(String userId);

    /**
     * ロールIDに基づいてメニューツリー情報を検索する
     *
     * @param roleId ロールID
     * @return 選択されたメニューリスト
     */
    public List<String> selectMenuListByRoleId(String roleId);

    /**
     * フロントエンドルートに必要なメニューを構築する
     *
     * @param menus メニューリスト
     * @return ルートリスト
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * フロントエンドに必要なツリーストラクチャを構築する
     *
     * @param menus メニューリスト
     * @return ツリーストラクチャリスト
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * フロントエンドに必要なドロップダウンツリーストラクチャを構築する
     *
     * @param menus メニューリスト
     * @return ドロップダウンツリーストラクチャリスト
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * メニューIDに基づいて情報を検索する
     *
     * @param menuId メニューID
     * @return メニュー情報
     */
    public SysMenu selectMenuById(String menuId);

    /**
     * メニューの子ノードが存在するかどうかを確認する
     *
     * @param menuId メニューID
     * @return 結果 true 存在する false 存在しない
     */
    public boolean hasChildByMenuId(String menuId);

    /**
     * メニューにロールが存在するかどうかを確認する
     *
     * @param menuId メニューID
     * @return 結果 true 存在する false 存在しない
     */
    public boolean checkMenuExistRole(String menuId);

    /**
     * メニュー情報を新規保存する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public int insertMenu(SysMenu menu);

    /**
     * メニュー情報を更新して保存する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public int updateMenu(SysMenu menu);

    /**
     * メニュー管理情報を削除する
     *
     * @param menuId メニューID
     * @return 結果
     */
    public int deleteMenuById(String menuId);

    /**
     * メニュー名称が一意であるかどうかを確認する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public boolean checkMenuNameUnique(SysMenu menu);
}
