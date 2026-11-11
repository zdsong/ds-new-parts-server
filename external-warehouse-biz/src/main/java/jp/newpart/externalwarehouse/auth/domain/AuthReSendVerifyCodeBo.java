package jp.newpart.externalwarehouse.auth.domain;

import jp.newpart.common.validator.annotation.LmNotBlank;
import lombok.Data;

/**
 * 認証コードを再送信
 */
@Data
public class AuthReSendVerifyCodeBo {

    /**
     * tempToken
     */
    @LmNotBlank(params = {"{tempToken}"})
    private String tempToken;

}
