package jp.newpart.common.biz.domain.vo;

import lombok.Data;

import java.util.Set;

/**
 * ログインユーザー
 */
@Data
public class UserInfoVo {

    /**
     * ユーザID
     */
    private String userId;

    /**
     * ユーザ名
     */
    private String userName;

    /**
     * 初回ログイン
     */
    private boolean isFirstLogin;

    /**
     * ユーザーの種類（00:CRM 90:Admin）
     */
    private String userType;

    /**
     * Role Id
     */
    private String roleId;

    /**
     * 権限List
     */
    private Set<String> permissions;
}
