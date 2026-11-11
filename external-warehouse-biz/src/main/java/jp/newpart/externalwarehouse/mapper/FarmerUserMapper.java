package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.core.domain.entity.FarmerUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 農家 ユーザーテーブル データ層
 *
 * @author nextosd
 */
public interface FarmerUserMapper {

    /**
     * ユーザーネームでユーザーを查詢
     *
     * @param userName ユーザーネーム
     * @return ユーザーオブジェクト情報
     */
    public FarmerUserInfo selectUserByUserName(String userName);

    /**
     * メールアドレスでユーザーを検索する
     *
     * @param mailAddress メールアドレス
     * @return ユーザーオブジェクト情報
     */
    public FarmerUserInfo selectUserByMailAddress(String mailAddress);

    /**
     * ユーザーIDでユーザーを查詢
     *
     * @param id ユーザーID
     * @return ユーザーオブジェクト情報
     */
    public FarmerUserInfo selectUserById(String id);

    /**
     * ユーザーパスワードをリセット
     *
     * @param id      ID
     * @param userPwd パスワード
     * @return 結果
     */
    public int resetUserPwd(@Param("id") String id, @Param("userPwd") String userPwd);

    /**
     * ユーザーのLogin情報を更新する
     *
     * @param user ユーザー情報
     * @return 結果
     */
    public int updateLoginInfo(FarmerUserInfo user);
}
