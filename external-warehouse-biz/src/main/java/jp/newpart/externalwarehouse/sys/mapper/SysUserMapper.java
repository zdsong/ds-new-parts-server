package jp.newpart.externalwarehouse.sys.mapper;

import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.externalwarehouse.sys.domain.bo.SysUserQueryBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ユーザーテーブル データ層
 *
 * @author nextosd
 */
public interface SysUserMapper {

    /**
     * 条件に応じてページ分割してユーザー一覧を查詢
     *
     * @param sysUser ユーザー情報
     * @return ユーザー情報集合情報
     */
    public List<SysUser> selectUserList(SysUserQueryBo sysUser);

    /**
     * 条件に応じてページ分割して既に割り当てられたユーザー役割一覧を查詢
     *
     * @param user ユーザー情報
     * @return ユーザー情報集合情報
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 条件に応じてページ分割して未割り当てのユーザー役割一覧を查詢
     *
     * @param user ユーザー情報
     * @return ユーザー情報集合情報
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * ユーザーネームでユーザーを查詢
     *
     * @param userName ユーザーネーム
     * @return ユーザーオブジェクト情報
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * ユーザーIDでユーザーを查詢
     *
     * @param userId ユーザーID
     * @return ユーザーオブジェクト情報
     */
    public SysUser selectUserById(String userId);

    /**
     * ユーザー情報を追加
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int insertUser(SysUser user);

    /**
     * ユーザー情報を更新
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateUser(SysUser user);

    /**
     * ユーザーパスワードを更新
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateUserPassword(SysUser user);

    /**
     * ユーザープロフィール画像を更新
     *
     * @param userName ユーザーネーム
     * @param avatar   プロフィール画像アドレス
     * @return 結果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * ユーザーパスワードをリセット
     *
     * @param userName ユーザーネーム
     * @param password パスワード
     * @return 結果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * ユーザーIDでユーザーを削除
     *
     * @param user ユーザー対象
     * @return 結果
     */
    public int deleteLogicUserByIdAndVersion(SysUser user);

    /**
     * ユーザーIDでユーザーを削除
     *
     * @param userId ユーザーID
     * @return 結果
     */
    public int deleteUserById(String userId);

    /**
     * ユーザー情報を一括削除
     *
     * @param userIds 削除するユーザーID
     * @return 結果
     */
    public int deleteUserByIds(String[] userIds);

    /**
     * ユーザーネームの一意性を検証
     *
     * @param userName ユーザーネーム
     * @return 結果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 電話番号の一意性を検証
     *
     * @param phonenumber 電話番号
     * @return 結果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * メールアドレスの一意性を検証
     *
     * @param email ユーザーのメールアドレス
     * @return 結果
     */
    public SysUser checkEmailUnique(String email);

    /**
     * 対応者コンボボックスデータを取得する
     * @return
     */
    List<SysUser> selectCrmUserList();
}
