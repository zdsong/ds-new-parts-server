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
public class OptionsAddressVo {
    /**
     * オプションのキー
     */
    private String key;

    /**
     * オプションの表示値
     */
    private String value;

}
