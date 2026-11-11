package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.annotation.EncryptedField;
import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 顧客マスタ (mast_customer)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastCustomerEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 顧客マスタ情報ID
     */
    private String id;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 氏名（姓）
     */
    @EncryptedField
    private String firstName;

    /**
     * 氏名（名）
     */
    @EncryptedField
    private String lastName;

    /**
     * 顧客名
     */
    @EncryptedField
    private String customerName;

    /**
     * 電話番号
     */
    @EncryptedField
    private String phoneNumber;

    /**
     * システム顧客番号
     */
    private String sysCustomerId;

    /**
     * 会員ID
     */
    private String memberId;

    /**
     * 氏名フリガナ（セイ）
     */
    @EncryptedField
    private String furiganaSei;

    /**
     * 氏名フリガナ（メイ）
     */
    @EncryptedField
    private String furiganaMei;

    /**
     * 会社名
     */
    @EncryptedField
    private String companyName;

    /**
     * 部署名
     */
    @EncryptedField
    private String departmentName;

    /**
     * 郵便番号
     */
    @EncryptedField
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
    @EncryptedField
    private String townName;

    /**
     * ビル等
     */
    @EncryptedField
    private String buildingEtc;

    /**
     * 住所
     */
    @EncryptedField
    private String address;

    /**
     * メールアドレス
     */
    @EncryptedField
    private String mailAddress;

    /**
     * 携帯電話番号
     */
    @EncryptedField
    private String mobile;

    /**
     * FAX番号
     */
    @EncryptedField
    private String fax;

    /**
     * 性別
     */
    @EncryptedField
    private String gender;

    /**
     * 生年月日
     */
    private String birth;

    /**
     * 備考
     */
    private String memo;

    /**
     * フリー項目１
     */
    private String freeItem1;

    /**
     * フリー項目２
     */
    private String freeItem2;

    /**
     * フリー項目３
     */
    private String freeItem3;

    /**
     * 顧客分類
     */
    private String customerCategory;

    /**
     * 確定ポイント
     */
    private String confirmedPoints;

    /**
     * 最終注文日
     */
    private String lastOrderDate;

    /**
     * 累計注文回数
     */
    private Integer totalOrderCnt;

    /**
     * 累計注文金額
     */
    private Integer totalOrderAmount;

    /**
     * 販促メール配信
     */
    private String promotionalEmailDistribution;

    /**
     * 会員パスワード
     */
    private String memberPwd;

    /**
     * 会員ランク固定種別
     */
    private String memberFlgFixedCategory;

    /**
     * 会員ランク
     */
    private String memberFlg;

    /**
     * 会員ランク説明内容
     */
    private String memberFlgDesc;

    /**
     * 予備１
     */
    private String rsv1;

    /**
     * 予備２
     */
    private String rsv2;

    /**
     * 予備３
     */
    private String rsv3;

    /**
     * システムソースフラグ
     */
    private String systemSourceFlg;

    /**
     * 廃止フラグ
     */
    private String discontinuedFlg;

    /**
     * Version
     */
    private Integer version;

}
