package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 農家らくうるカートマスタ (mast_farmer_rakuuru_cart)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastFarmerRakuuruCartEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 農家らくうるカートID
     */
    private String frid;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * らくうるカートID
     */
    @EncryptedField
    private String rakuuruCartId;

    /**
     * らくうるカートパスワード
     */
    @EncryptedField
    private String rakuuruCartPwd;

    /**
     * らくうるカートタイプ
     */
    private String rakuuruCartType;

    /**
     * Version
     */
    private Integer version;

}
