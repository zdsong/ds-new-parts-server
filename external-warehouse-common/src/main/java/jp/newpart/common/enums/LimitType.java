package jp.newpart.common.enums;

/**
 * 限流タイプ
 *
 * @author nextosd
 */

public enum LimitType
{
    /**
     * デフォルトポリシーのフルスロットルフロー
     */
    DEFAULT,

    /**
     * 要求者IPによるストリーミング制限
     */
    IP
}
