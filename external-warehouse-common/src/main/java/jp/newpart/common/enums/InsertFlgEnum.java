package jp.newpart.common.enums;

import lombok.Getter;

/**
 * 新しい規制/改正 のコード定数
 * 1:新規 0:編集
 *
 * @author nextosd
 */
@Getter
public enum InsertFlgEnum {
    /**
     * 0:編集
     */
    OFF("0"),
    /**
     * 1:新規
     */
    ON("1");

    private final String code;

    InsertFlgEnum(final String code) {
        this.code = code;
    }

}