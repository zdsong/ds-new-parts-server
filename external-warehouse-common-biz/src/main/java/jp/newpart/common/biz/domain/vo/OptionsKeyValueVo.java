package jp.newpart.common.biz.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * コンボボックスのオプション対象
 *
 * @author nextosd
 */
@Builder
@Data
public class OptionsKeyValueVo {
    /**
     * オプションのキー
     */
    private String key;

    /**
     * オプションの表示値
     */
    private String value;

    /**
     * デフォルト（Y/N）Y=Yes、N=No
     */
    private String isDefault;

    /**
     * スペア１
     */
    private String spare1;

    /**
     * スペア2
     */
    private String spare2;

    /**
     * スペア3
     */
    private String spare3;

    /**
     * スペア4
     */
    private String spare4;

    /**
     * スペア5
     */
    private String spare5;
}
