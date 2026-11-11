package jp.newpart.common.biz.domain.bo;

import lombok.Data;

/**
 * オプションの検索条件
 *
 * @author nextosd
 */
@Data
public class OptionsParamBo {

    /**
     * コンボボックス定数
     */
    private String cmdOptionName;

    /**
     * 取得条件1
     */
    private String conditionValue1;

    /**
     * 取得条件2
     */
    private String conditionValue2;

    /**
     * 取得条件3
     */
    private String conditionValue3;

    /**
     * 取得条件4
     */
    private String conditionValue4;

    /**
     * 取得条件5
     */
    private String conditionValue5;
}
