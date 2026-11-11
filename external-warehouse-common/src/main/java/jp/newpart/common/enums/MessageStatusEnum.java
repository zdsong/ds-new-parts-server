package jp.newpart.common.enums;

import lombok.Getter;

/**
 * メッセージのステータス コード定数
 */
@Getter
public enum MessageStatusEnum {
    PROCESS("0", "対応中"), RESOLVED("1", "終了");

    private final String code;
    private final String info;

    MessageStatusEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
