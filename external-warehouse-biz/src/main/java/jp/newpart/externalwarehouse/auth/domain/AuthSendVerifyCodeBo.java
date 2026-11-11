package jp.newpart.externalwarehouse.biz.auth.domain;

import jp.newpart.common.validator.annotation.LmNotBlank;
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
