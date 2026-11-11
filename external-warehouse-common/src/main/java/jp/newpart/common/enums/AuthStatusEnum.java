package jp.newpart.common.enums;

/**
 * 二次認証
 */
public enum AuthStatusEnum {
    PENDING_FIRST_FACTOR,    // 一次認証
    PENDING_SECOND_FACTOR,   // 二次認証
    COMPLETED                // 認証完了
}
