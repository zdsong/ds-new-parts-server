package jp.newpart.common.core.domain.model;

import lombok.Data;

/**
 * ユーザーログインオブジェクト
 * 
 * @author nextosd
 */
@Data
public class LoginBody
{
    /**
     * User Name
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Code
     */
    private String code;

    /**
     * UUID
     */
    private String uuid;
}
