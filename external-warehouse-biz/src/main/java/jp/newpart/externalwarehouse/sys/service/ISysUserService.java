package jp.newpart.externalwarehouse.sys.service;

import jp.newpart.common.biz.domain.vo.OptionsKeyValueVo;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.externalwarehouse.sys.domain.bo.SysUserQueryBo;

import java.util.List;

/**
 * ユーザー ビジネス層
 *
 * @author nextosd
 */
public interface ISysUserService {
    /**
     * 条件に基づいてページ分割されたユーザー一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    public List<SysUser> selectUserList(SysUserQueryBo user);

    /**
     * 条件に基づいてページ分割された割り当て済みユーザー役割一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 条件に基づいてページ分割された未割り当てユーザー役割一覧を取得する
     *
     * @param user ユーザー情報
     * @return ユーザー情報のリスト
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * ユーザー名でユーザーを検索する
     *
     * @param userName ユーザー名
     * @return ユーザー情報
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * ユーザーIDでユーザーを検索する
     *
     * @param userId ユーザーID
     * @return ユーザー情報
     */
    public SysUser selectUserById(String userId);

    /**
     * ユーザーIDに基づいてユーザーが所属する役割グループを取得する
     *
     * @param userName ユーザー名
     * @return 結果
     */
    public String selectUserRoleGroup(String userName);

    /**
     * ユーザー名が一意であるか確認する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * 電話番号が一意であるか確認する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     * Eメールが一意であるか確認する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     * ユーザーが操作を許可されているか確認する
     *
     * @param user ユーザー情報
     */
    public void checkUserAllowed(SysUser user);

    /**
     * ユーザーがデータ権限を持っているか確認する
     *
     * @param userId ユーザーID
     */
    public void checkUserDataScope(String userId);

    /**
     * 新規ユーザー情報を追加する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int insertUser(SysUser user);

    /**
     * ユーザー情報を登録する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public boolean registerUser(SysUser user);

    /**
     * ユーザー情報を更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateUser(SysUser user);

    /**
     * ユーザーに役割を付与する
     *
     * @param userId  ユーザーID
     * @param roleIds 役割グループ
     */
    public void insertUserAuth(String userId, String[] roleIds);

    /**
     * ユーザーのステータスを更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateUserStatus(SysUser user);

    /**
     * ユーザーの基本情報を更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateUserProfile(SysUser user);

    /**
     * ユーザーのプロフィール画像を更新する
     *
     * @param userName ユーザー名
     * @param avatar   プロフィール画像のURL
     * @return 結果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * ユーザーのパスワードをリセットする
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int resetPwd(SysUser user);

    /**
     * ユーザーのパスワードをリセットする
     *
     * @param userName ユーザー名
     * @param password パスワード
     * @return 結果
     */
    public int resetUserPwd(String userName, String password);

    /**
     * ユーザーIDでユーザーを削除する
     *
     * @param user ユーザー対象
     * @return 結果
     */
    public int deleteLogicUserByIdAndVersion(SysUser user);

    /**
     * ユーザーIDに基づいてユーザーを削除する
     *
     * @param userId ユーザーID
     * @return 結果
     */
    public int deleteUserById(String userId);

    /**
     * ユーザー情報を一括削除する
     *
     * @param userIds 削除するユーザーIDのリスト
     * @return 結果
     */
    public int deleteUserByIds(String[] userIds);

    /**
     * ユーザーデータをインポートする
     *
     * @param userList        ユーザーデータのリスト
     * @param isUpdateSupport 更新をサポートするかどうか、既存の場合データを更新する
     * @param operName        操作ユーザー
     * @return 結果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 対応者コンボボックスデータを取得する
     * @return
     */
    List<OptionsKeyValueVo> getCrmUserOptions();
}
