package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 農家マスタ (mast_farmer)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastFarmerEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 農家マスタ情報ID
     */
    private String id;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 農家名
     */
    private String farmerName;

    /**
     * 農家名（カナ）
     */
    private String farmerNameKn;

    /**
     * 電話番号
     */
    @EncryptedField
    private String phoneNumber;

    /**
     * メールアドレス
     */
    @EncryptedField
    private String mailAddress;

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
     * 町域・番地
     */
    private String townName;

    /**
     * ビル等
     */
    private String buildingEtc;

    /**
     * 住所
     */
    private String address;

    /**
     * 代表者名
     */
    @EncryptedField
    private String representativeName;

    /**
     * 代表者名（カナ）
     */
    @EncryptedField
    private String representativeNameKn;

    /**
     * 発行者
     */
    private String issuedBy;

    /**
     * 発行日
     */
    private String issuedDate;

    /**
     * ログイン回数
     */
    private Integer loginCnt;

    /**
     * パスワード
     */
    private String userPassword;

    /**
     * 利用状態
     */
    private String usageStatus;

    /**
     * 廃止フラグ
     */
    private String discontinuedFlg;

    /**
     * Version
     */
    private Integer version;

}
