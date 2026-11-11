package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.newpart.common.annotation.EncryptedField;

/**
 * 問合せ情報 (bss_ask)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssAskEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 問合せ情報ID
     */
    private String id;

    /**
     * 問合せNo.
     */
    private String askNo;

    /**
     * 問合せ受付日
     */
    private String askDate;

    /**
     * 問合せ経路
     */
    private String askWay;

    /**
     * 問合せ種別
     */
    private String askType;

    /**
     * お問い合わせ種別
     */
    private String category1;

    /**
     * 問い合わせ項目
     */
    private String category2;

    /**
     * ステータス
     */
    private String status;

    /**
     * お問い合わせ・対応内容
     */
    private String askAnswerRemark;

    /**
     * 回答内容
     */
    private String answer;

    /**
     * お問い合わせ者
     */
    @EncryptedField
    private String askUser;

    /**
     * お問い合わせ者区分
     */
    private String askUserType;

    /**
     * 最終対応者
     */
    private String finalAnswerUser;

    /**
     * 最終更新日時
     */
    private String finalUpdateTime;

    /**
     * システム顧客番号
     */
    private String sysCustomerId;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 電話番号
     */
    @EncryptedField
    private String phoneNumber;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 注文番号
     */
    private String orderId;

    /**
     * ユーザーID
     */
    private String userCd;

    /**
     * Version
     */
    private Integer version;

}
