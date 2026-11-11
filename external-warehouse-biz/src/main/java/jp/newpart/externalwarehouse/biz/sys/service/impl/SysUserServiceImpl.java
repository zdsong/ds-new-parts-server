package jp.newpart.externalwarehouse.biz.sys.service.impl;

import jakarta.validation.Validator;
import jp.newpart.common.biz.domain.vo.OptionsKeyValueVo;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysRole;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.bean.BeanValidators;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.sys.domain.SysUserRole;
import jp.newpart.externalwarehouse.biz.sys.domain.bo.SysUserQueryBo;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysRoleMapper;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysUserMapper;
import jp.newpart.externalwarehouse.biz.sys.mapper.SysUserRoleMapper;
import jp.newpart.externalwarehouse.biz.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ユーザー ビジネス層処理
 *
 * @author nextosd
 */
@Service
@Slf4j
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    protected Validator validator;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 条件に基づいてページ分割してユーザー一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    @Override
    public List<SysUser> selectUserList(SysUserQueryBo user) {
        // 利用状態
        if(ValueUtils.isNotEmpty(user.getStatus())){
            user.setStatusList(Arrays.asList(ConvertUtils.toStrArray(user.getStatus())));
        }
        // 権限
        if(ValueUtils.isNotEmpty(user.getRoleId())){
            user.setRoleIdList(Arrays.asList(ConvertUtils.toStrArray(user.getRoleId())));
        }

        return userMapper.selectUserList(user);
    }

    /**
     * ユーザーIDでユーザーを削除する
     *
     * @param user ユーザー対象
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLogicUserByIdAndVersion(SysUser user) {
        String userId = user.getUserId();
        // ユーザーと役割の関連を削除
        userRoleMapper.deleteUserRoleByUserId(userId);
//        // ユーザーと職位の関連を削除
//        userPostMapper.deleteUserPostByUserId(userId);

        int result = userMapper.deleteLogicUserByIdAndVersion(user);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * 条件に基づいてページ分割して割り当てられたユーザー役割一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 条件に基づいてページ分割して未割り当てのユーザー役割一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * ユーザー名でユーザーを検索する
     *
     * @param userName ユーザー名
     * @return ユーザーオブジェクト情報
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * ユーザーIDでユーザーを検索する
     *
     * @param userId ユーザーID
     * @return ユーザーオブジェクト情報
     */
    @Override
    public SysUser selectUserById(String userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * ユーザーが所属する役割グループを取得する
     *
     * @param userName ユーザー名
     * @return 結果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * ユーザー名が一意かどうかを検証する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        String userId = user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 電話番号が一意かどうかを検証する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        String userId = user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * emailが一意かどうかを検証する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        String userId = user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * ユーザーが操作を許可されているかどうかを検証する
     *
     * @param user ユーザー情報
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            // システム管理者のユーザーには操作を許可しません
            throw new ServiceException(MessageUtils.message("E0046"));
        }
    }

    /**
     * ユーザーにデータ権限があるかどうかを検証する
     *
     * @param userId ユーザーID
     */
    @Override
    public void checkUserDataScope(String userId) {
//        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
        SysUserQueryBo user = new SysUserQueryBo();
        user.setUserId(userId);
        List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
        if (StringUtils.isEmpty(users)) {
            // 該当データは既に別のユーザーより削除されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
//        }
    }

    /**
     * 新規ユーザー情報を保存する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        // 新規ユーザー情報を追加
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserId(IdUtils.nextIdStr());
        // データを暗号化
        this.encodeData(user);

        int rows = userMapper.insertUser(user);
        insertUserRoleByRoleId(user);
        return rows;
    }

    /**
     * ユーザー情報を登録する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * ユーザー情報を更新して保存する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        String userId = user.getUserId();
        // ユーザーと役割の関連を削除
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新規ユーザーと役割の管理を追加
        insertUserRoleByRoleId(user);

        // データを暗号化
        this.encodeData(user);

        int result = userMapper.updateUser(user);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * ユーザーに役割を付与する
     *
     * @param userId  ユーザーID
     * @param roleIds 役割グループ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(String userId, String[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * ユーザーのステータスを更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        int result = userMapper.updateUser(user);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * ユーザーの基本情報を更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * ユーザーのプロフィール画像を更新する
     *
     * @param userName ユーザー名
     * @param avatar   プロフィール画像のURL
     * @return 結果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * ユーザーのパスワードをリセットする
     * 管理者リセット
     *
     * @param user ユーザー情報
     * @return 結果
     */
    @Override
    public int resetPwd(SysUser user) {
        int result = userMapper.updateUserPassword(user);
        if (result < 1) {
            // 該当データは既に別のユーザーより更新されました。
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        return result;
    }

    /**
     * ユーザーのパスワードをリセットする
     * ユーザーリセット
     *
     * @param userName ユーザー名
     * @param password パスワード
     * @return 結果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新規ユーザー役割情報を追加する
     *
     * @param user ユーザーオブジェクト
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新規ユーザー役割情報を追加する
     *
     * @param user ユーザーオブジェクト
     */
    public void insertUserRoleByRoleId(SysUser user) {
        if (StringUtils.isNotNull(user.getRoleId())) {
            // 役割グループを取得
            String[] roleIds = {user.getRoleId()};
            // 新規ユーザーと役割の管理を追加
            insertUserRole(user.getUserId(), roleIds);
        }
    }

    /**
     * 新規ユーザー役割情報を追加する
     *
     * @param userId  ユーザーID
     * @param roleIds 役割グループ
     */
    public void insertUserRole(String userId, String[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新規ユーザーと役割の管理を追加
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (String roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * ユーザーIDでユーザーを削除する
     *
     * @param userId ユーザーID
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(String userId) {
        // ユーザーと役割の関連を削除
        userRoleMapper.deleteUserRoleByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * ユーザー情報を一括削除する
     *
     * @param userIds 削除するユーザーID
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(String[] userIds) {
        for (String userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // ユーザーと役割の関連を削除
        userRoleMapper.deleteUserRole(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * ユーザーデータをインポートする
     *
     * @param userList        ユーザーデータリスト
     * @param isUpdateSupport 更新をサポートするかどうか、既存の場合はデータを更新
     * @param operName        操作ユーザー
     * @return 結果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("インポートするユーザーデータが空であってはなりません！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // そのユーザーが存在するかどうかを確認
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setUserId(IdUtils.nextIdStr());
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、アカウント " + user.getUserName() + " インポート成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、アカウント " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、アカウント " + user.getUserName() + " 既に存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、アカウント " + user.getUserName() + " インポート失敗：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "申し訳ありません、インポートに失敗しました！合計 " + failureNum + " 件のデータ形式が正しくない、エラーは以下の通りです：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "おめでとうございます、データがすべて正常にインポートされました！合計 " + successNum + " 件、データは以下の通りです：");
        }
        return successMsg.toString();
    }

    /**
     * 対応者コンボボックスデータを取得する
     * @return
     */
    @Override
    public List<OptionsKeyValueVo> getCrmUserOptions() {
        List<SysUser> sysUserList = userMapper.selectCrmUserList();
        if(ValueUtils.isNotEmpty(sysUserList)){

            return sysUserList.stream()
                    .map(item -> OptionsKeyValueVo.builder()
                            .key(item.getUserName())
                            .value(item.getNickName()
                            ).build())
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * データを暗号化
     * @param user システムユーザー
     */
    private void encodeData(SysUser user) {
        // データを暗号化
        if(ValueUtils.isNotEmpty(user.getEmail())){
            user.setEmail(DecryptUtils.encryptAES(user.getEmail()));
        }
        if(ValueUtils.isNotEmpty(user.getPhonenumber())){
            user.setPhonenumber(DecryptUtils.encryptAES(user.getPhonenumber()));
        }
    }
}
