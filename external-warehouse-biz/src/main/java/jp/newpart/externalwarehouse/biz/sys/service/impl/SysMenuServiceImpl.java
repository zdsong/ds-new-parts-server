package jp.newpart.externalwarehouse.biz.sys.service.impl;

import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.TreeSelect;
import jp.newpart.common.core.domain.entity.SysMenu;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.sys.domain.vo.MetaVo;
import jp.newpart.externalwarehouse.biz.sys.domain.vo.RouterVo;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysMenuMapper;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysRoleMapper;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysRoleMenuMapper;
import jp.newpart.externalwarehouse.biz.sys.service.ISysMenuService;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * メニュー ビジネス層処理
 *
 * @author nextosd
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * ユーザーIDに基づいてシステムメニュー一覧を取得する
     *
     * @param userId ユーザーID
     * @return メニュー一覧
     */
    @Override
    public List<SysMenu> selectMenuList(String userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * システムメニュー一覧を取得する
     *
     * @param menu メニュー情報
     * @return メニュー一覧
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, String userId) {
        List<SysMenu> menuList = null;
        // 管理者はすべてのメニュー情報を表示
        if ("1".equals(userId) || SecurityUtils.isAdmin()) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * ユーザーIDに基づいて権限を取得する
     *
     * @param userId ユーザーID
     * @return 権限一覧
     */
    @Override
    public Set<String> selectMenuPermsByUserId(String userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 役割IDに基づいて権限を取得する
     *
     * @param roleId 役割ID
     * @return 権限一覧
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(String roleId) {
        List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * ユーザーIDに基づいてメニューを取得する
     *
     * @param userId ユーザーID
     * @return メニュー一覧
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(String userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, "0");
    }

    /**
     * 役割IDに基づいてメニューツリー構造情報を取得する
     *
     * @param roleId 役割ID
     * @return 選択されたメニュー一覧
     */
    @Override
    public List<String> selectMenuListByRoleId(String roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, (ValueUtils.isNotEmpty(role.getMenuCheckStrictly()) && role.getMenuCheckStrictly() == 1));
    }

    /**
     * フロントエンドルートに必要なメニューを構築する
     *
     * @param menus メニュー一覧
     * @return ルート一覧
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), isNo(menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), isNo(menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().equals("0") && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * フロントエンドに必要なツリー構造を構築する
     *
     * @param menus メニュー一覧
     * @return ツリー構造一覧
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<String> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // トップレベルノードの場合、その親ノードのすべての子ノードを再帰的に取得
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * フロントエンドに必要なドロップダウンツリー構造を構築する
     *
     * @param menus メニュー一覧
     * @return ドロップダウンツリー構造一覧
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * メニューIDに基づいて情報を取得する
     *
     * @param menuId メニューID
     * @return メニュー情報
     */
    @Override
    public SysMenu selectMenuById(String menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * メニュー子ノードが存在するかどうかを確認する
     *
     * @param menuId メニューID
     * @return 結果
     */
    @Override
    public boolean hasChildByMenuId(String menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * メニュー使用数を查詢する
     *
     * @param menuId メニューID
     * @return 結果
     */
    @Override
    public boolean checkMenuExistRole(String menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * メニュー情報を新規保存する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        menu.setMenuId(IdUtils.nextIdStr());
        return menuMapper.insertMenu(menu);
    }

    /**
     * メニュー情報を更新して保存する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * メニュー管理情報を削除する
     *
     * @param menuId メニューID
     * @return 結果
     */
    @Override
    public int deleteMenuById(String menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * メニューネームが一意かどうかを検証する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        String menuId = menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        // 編集時は自分自身のレコードがヒットするため、メニューID が一致する場合は重複とみなさない。
        // ID は文字列（19 桁の雪花IDを含む）で比較する。
        if (StringUtils.isNotNull(info) && !StringUtils.equals(info.getMenuId(), menuId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * ルート名を取得する
     *
     * @param menu メニュー情報
     * @return ルート名
     */
    public String getRouteName(SysMenu menu) {
        // 外部リンクでなく、トップレベルディレクトリ（タイプがディレクトリ）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * ルート名を取得する（ルート名が設定されていない場合はルートパスを使用）
     *
     * @param name ルート名
     * @param path ルートパス
     * @return ルート名（キャメルケース）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * ルートパスを取得する
     *
     * @param menu メニュー情報
     * @return ルートパス
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内部リンクで外部サイトを開く場合
        if (!menu.getParentId().equals("0") && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 外部リンクでなく、トップレベルディレクトリ（タイプがディレクトリ）
        if ("0".equals(menu.getParentId()) && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && isNo(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 外部リンクでなく、トップレベルディレクトリ（タイプがメニュー）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * コンポーネント情報を取得する
     *
     * @param menu メニュー情報
     * @return コンポーネント情報
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && !menu.getParentId().equals("0") && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * メニューが内部遷移かどうかを確認する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().equals("0") && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && isNo(menu.getIsFrame());
    }

    /**
     * is_frame / is_cache が「いいえ」かどうかを判定する。
     * 既存データは '1'（旧: いいえ）、汎用コード sys_yes_no は 'N' のため、両方を「いいえ」として扱う。
     *
     * @param value is_frame / is_cache の値
     * @return 「いいえ」の場合 true
     */
    private boolean isNo(String value) {
        return UserConstants.NO_FRAME.equals(value) || "N".equals(value);
    }

    /**
     * メニューが内部リンクコンポーネントかどうかを確認する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public boolean isInnerLink(SysMenu menu) {
        return isNo(menu.getIsFrame()) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * メニューがparent_viewコンポーネントかどうかを確認する
     *
     * @param menu メニュー情報
     * @return 結果
     */
    public boolean isParentView(SysMenu menu) {
        return !menu.getParentId().equals("0") && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 親ノードのIDに基づいてすべての子ノードを取得する
     *
     * @param list     分類表
     * @param parentId 親ノードID
     * @return 子ノード一覧
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 伝入の親ノードIDに基づいて、その親ノードのすべての子ノードを取得
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 再帰リスト
     *
     * @param list 分類表
     * @param t    子ノード
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 子ノードリストを取得
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 子ノードリストを取得
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 子ノードがあるかどうかを判断
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * インターナリンクドメインの特殊文字を置換
     *
     * @return 置換後のインターナリンクドメイン
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}
