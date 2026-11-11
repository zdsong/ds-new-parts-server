package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

/**
 * 顧客マスタ インポート CSV Data
 */
@Data
public class MastCustomerImportBo {

//    /**
//     * 顧客マスタ情報ID
//     */
//    private String id;
//
//    /**
//     * 顧客ID
//     */
//    private String customerId;

    /**
     * システム顧客番号
     */
    private String sysCustomerId;

    /**
     * 会員ID
     */
    private String memberId;

    /**
     * 氏名（姓）
     */
    private String firstName;

    /**
     * 氏名（名）
     */
    private String lastName;

    /**
     * 顧客名
     */
    //private String customerName;

    /**
     * 氏名フリガナ（セイ）
     */
    private String furiganaSei;

    /**
     * 氏名フリガナ（メイ）
     */
    private String furiganaMei;

    /**
     * 会社名
     */
    private String companyName;

    /**
     * 部署名
     */
    private String departmentName;

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
   // private String address;

    /**
     * メールアドレス
     */
    private String mailAddress;

    /**
     * 電話番号
     */
    private String phoneNumber;

    /**
     * 携帯電話番号
     */
    private String mobile;

    /**
     * FAX番号
     */
    private String fax;

    /**
     * 性別
     */
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
    //private String memberFlgDesc;

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
}
