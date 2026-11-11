package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 顧客マスタ サービスパラメータ対象
 *
 * @author nextosd
 */
@Data
public class MastCustomerSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 顧客名
     */
    private String customerName;

    /**
     * 電話番号
     */
    private String phoneNumber;

    /**
     * 郵便番号
     */
    private String postalCode;

    /**
     * 都道府県
     */
    private String kenName;

    /**
     * 市区町村
     */
    private String cityName;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 会員ID
     */
    private String memberId;
}
