package jp.newpart.externalwarehouse.biz.sys.domain;

import jp.newpart.common.annotation.DecryptField;
import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.validator.annotation.LmNotBlank;
import jp.newpart.common.validator.annotation.LmPassword;
import jp.newpart.common.validator.utils.ValidatorGroup;
import lombok.Data;

/**
 * ユーザーオブジェクト sys_user
 *
 * @author nextosd
 */
@Data
public class SysUserParam {

    /**
     * ユーザーの古いパスワード
     */
    @DecryptField
    @LmNotBlank(params = {"{oldPassword}"}, groups = {ValidatorGroup.AddGroup.class})
    private String oldPassword;

    /**
     * ユーザーの新パスワード
     */
    @DecryptField
    @LmNotBlank(params = {"{newPassword}"}, groups = {ValidatorGroup.AddGroup.class})
    @LmLength(min = 8, max = 20, params = {"{password}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class})
    @LmPassword(params = {"{newPassword}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class})
    private String newPassword;
}
