package jp.newpart.externalwarehouse.biz.auth.domain;

import lombok.Data;

/**
 * 一時トークン
 */
@Data
public class AuthTempTokenBo {

    /**
     * 認証コードを送信 ID
     */
    private String sendVerifyCodeId;

    /**
     * ユーザーID
     */
    private String userId;
}
