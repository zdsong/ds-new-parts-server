package jp.newpart.common.enums;

import lombok.Getter;

/**
 * システムフラグコード定数
 * 1:システム 0:非システム
 *
 * @author nextosd
 */
@Getter
public enum SystemFlgEnum {
    /**
     * 0:非システム
     */
    NON_SYS("0"),
    /**
     * 1:システム
     */
    SYS("1");

    private final String code;

    SystemFlgEnum(final String code) {
        this.code = code;
    }

}