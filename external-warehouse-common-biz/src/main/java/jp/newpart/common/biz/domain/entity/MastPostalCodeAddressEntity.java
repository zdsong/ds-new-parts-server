package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 郵便番号アドレスマスタ  (mast_postal_code_address)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastPostalCodeAddressEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 郵便番号
     */
    private String postalCode;

    /**
     * 都道府県名
     */
    private String kenName;

    /**
     * 市区町村名
     */
    private String cityName;

    /**
     * 町域名
     */
    private String townName;

    /**
     * 都道府県名ローマ字
     */
    private String kenNameRomanLetters;

    /**
     * 市区町村名ローマ字
     */
    private String cityNameRomanLetters;

    /**
     * 町域名ローマ字
     */
    private String townNameRomanLetters;

}
