package jp.nasta.pms.externalwarehouse.auth.domain;

import jp.nasta.pms.common.validator.annotation.LmNotBlank;
import lombok.Data;

/**
 * 認証コードを送信
 */
@Data
public class AuthSendVerifyCodeBo {

    /**
     * メールアドレス
     */
    @LmNotBlank(params = {"{mailAddress}"})
    private String mailAddress;
}
