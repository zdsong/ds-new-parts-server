package jp.nasta.pms.externalwarehouse.auth.domain;

import jp.nasta.pms.common.validator.annotation.LmNotBlank;
import lombok.Data;

/**
 * 二次認証
 */
@Data
public class AuthSecondVerifyBo {

    /**
     * tempToken
     */
    @LmNotBlank(params = {"{tempToken}"})
    private String tempToken;

    /**
     * 認証コード
     */
    @LmNotBlank(params = {"{verifyCode}"})
    private String verifyCode;
}
