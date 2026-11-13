package jp.newpart.externalwarehouse.sys.service.impl;

import jp.newpart.common.cache.AppCache;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.externalwarehouse.sys.domain.SysRoleMenu;
import jp.newpart.externalwarehouse.sys.domain.SysUserRole;
import jp.newpart.externalwarehouse.sys.mapper.SysMenuMapper;
import jp.newpart.externalwarehouse.sys.mapper.SysRoleMapper;
import jp.newpart.externalwarehouse.sys.mapper.SysRoleMenuMapper;
import jp.newpart.externalwarehouse.sys.mapper.SysUserRoleMapper;
import jp.newpart.externalwarehouse.sys.service.ISysRoleService;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ロールビジネス処理
 * 
 * @author nextosd
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private AppCache cacheService;

    /**
     * 条件付きページングによるロールデータを取得する
     *
     * @param role ロール情報
     * @return ロールデータリスト情報
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * ユーザーIDにロールリスト情報を取得する
     *
     * @param userId ユーザーID
     * @return ロールリスト
     */
    @Override
    public List<SysRole> selectRolesByUserId(String userId)
    {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().equals(userRole.getRoleId()))
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * ユーザーIDにロール権限リストを取得する
     *
     * @param userId ユーザーID
     * @return 権限リスト
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * すべてロールを取得する
     *
     * @return ロールリスト
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * ユーザーIDからロール選択ボックスのリストを取得する
     *
     * @param userId ユーザーID
     * @return ロールリスト
     */
    @Override
    public List<String> selectRoleListByUserId(String userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * ロールIDによるロールを取得する
     *
     * @param roleId ロールID
     * @return ロール情報
     */
    @Override
    public SysRole selectRoleById(String roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 役割名称の一意性を確認する
     *
     * @param role 役割情報
     * @return 結果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        String roleId = role.getRoleId();
        Integer count = roleMapper.checkRoleNameUnique(roleId, role.getRoleName());
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        // デフォルトのロールキー情報を設定
        if(ValueUtils.isEmpty(role.getRoleKey())) {
            role.setRoleKey(IdUtils.nextIdStr("R"));
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 役割権限の一意性を確認する
     *
     * @param role 役割情報
     * @return 結果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        String roleId = role.getRoleId();
        Integer count = roleMapper.checkRoleKeyUnique(roleId, role.getRoleKey());
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * ロールが操作を許可するかどうかを検証する
     *
     * @param role ロール情報
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new ServiceException("スーパーアドミンの役割を操作することはできません");
        }
    }

    /**
     * ロールにデータ権限があるかどうかを検証する
     *
     * @param roleIds ロールID
     */
    @Override
    public void checkRoleDataScope(String... roleIds)
    {
        if (!SecurityUtils.isAdmin())
        {
            for (String roleId : roleIds)
            {
                SysRole role = new SysRole();
                role.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                if (StringUtils.isEmpty(roles))
                {
                    throw new ServiceException("ロールデータにアクセスする権限がありません.");
                }
            }
        }
    }

    /**
     * ロールIDによるロール使用量を取得する
     *
     * @param roleId ロールID
     * @return
     */
    @Override
    public int countUserRoleByRoleId(String roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * ロール情報を新規する
     *
     * @param role ロール情報
     * @return
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        role.setRoleId(IdUtils.nextIdStr());
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * ロール情報を変更する
     *
     * @param role ロール情報
     * @return
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        // 役割情報の更新
        int result = roleMapper.updateRole(role);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        // ロールの削除メニューへの関連付け
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * ロールステータスを変更する
     *
     * @param role ロール情報
     * @return
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * ロールメニュー情報を新規する
     * 
     * @param role ロール
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;

        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (String menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * ロールIDによるロールを削除する
     *
     * @param roleId ロールID
     * @return
     */
    @Override
    @Transactional
    public int deleteRoleById(String roleId)
    {
        // ロールの削除メニューへの関連付け
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * ロール情報の一括削除
     *
     * @param roleIds ロールID
     * @return
     */
    @Override
    @Transactional
    public int deleteRoleByIds(String[] roleIds)
    {
        for (String roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new ServiceException(String.format("%1$sは割り当てられており、削除できません", role.getRoleName()));
            }
        }
        // ロールの削除メニューへの関連付け
        roleMenuMapper.deleteRoleMenu(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * ユーザーロールの権限解除
     *
     * @param userRole ユーザーとロールの関連情報
     * @return
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * ユーザーロールの一括認可解除
     *
     * @param roleId ロールID
     * @param userIds ユーザーID
     * @return
     */
    @Override
    public int deleteAuthUsers(String roleId, String[] userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     *  ユーザーロールの一括設定
     *
     * @param roleId ロールID
     * @param userIds ユーザーID
     * @return
     */
    @Override
    public int insertAuthUsers(String roleId, String[] userIds)
    {
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (String userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

    /**
     * 役割IDを介して役割を削除する
     *
     * @param sysRole 役割対象
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLogicRoleByIdAndVersion(SysRole sysRole) {
        String roleId = sysRole.getRoleId();
        // 役割とメニューの関連を物理削除
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        SysRole role = roleMapper.selectRoleByIdAndDelFlag(roleId);
        if (countUserRoleByRoleId(roleId) > 0) {
            // ロールが割り当てられているため、削除できません。
            throw new ServiceException(String.format(MessageUtils.message("E0018", role.getRoleName())));
        }
        //  役割論理削除
        int result = roleMapper.deleteLogicRoleByIdAndVersion(sysRole);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * ロール情報をキャッシュする
     *
     * @param roleId
     */
    @Override
    public void setRoleInfoToCache(String roleId) {
        if (roleId == null) {
            return;
        }
        // キャッシュキーを設定する
        String cacheKey = getCacheKey(roleId);

        // 役割情報をキャッシュ化する
        SysRole role = roleMapper.selectRoleByIdAndDelFlag(roleId);
        if (role != null) {
            if ("0".equals(role.getDelFlag())) {
                // 役割情報をキャッシュ化する
                List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
                cacheService.setCacheObject(cacheKey, perms);
                return;
            }
        }
        cacheService.deleteObject(cacheKey);
    }

    /**
     * キャッシュキーを設定する
     *
     * @param roleId パラメータキー
     * @return キャッシュキー
     */
    private String getCacheKey(String roleId) {
        return CacheConstants.SYS_ROLE_MENU_PERM_KEY.concat(roleId);
    }
}
