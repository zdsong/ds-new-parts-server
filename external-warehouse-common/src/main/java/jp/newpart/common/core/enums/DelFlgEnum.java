package jp.newpart.common.core.enums;

import lombok.Getter;

/**
 * 削除フラグ/廃止フラグ のコード定数
 * 1:廃止 0:有効
 * 1:削除する 0:未削除
 *
 * @author nextosd
 */
@Getter
public enum DelFlgEnum {
    /**
     * 0:未削除
     */
    OFF("0"),
    /**
     * 1:削除する
     */
    ON("1");

    private final String code;

    DelFlgEnum(final String code) {
        this.code = code;
    }

}