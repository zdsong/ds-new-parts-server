package jp.newpart.externalwarehouse.biz.auth.domain;

import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.validator.annotation.LmNotBlank;
import jp.newpart.common.validator.annotation.LmPassword;
import jp.newpart.common.validator.utils.ValidatorGroup;
import lombok.Data;

/**
 * パスワード再設定
 */
@Data
public class AuthResetPwdBo {

    /**
     * 新しいパスワード
     */
    @LmNotBlank(params = {"{newPassword}"}, groups = {ValidatorGroup.AddGroup.class})
    @LmLength(min = 8, max = 20, params = {"{newPassword}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class})
    @LmPassword(params = {"{newPassword}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class})
    private String newPassword;

    /**
     * 新しいパスワード（確認用）
     */
    @LmNotBlank(params = {"{confimPassword}"})
    private String confimPassword;
}
