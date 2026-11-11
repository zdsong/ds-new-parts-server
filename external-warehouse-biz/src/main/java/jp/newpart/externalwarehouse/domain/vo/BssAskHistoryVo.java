package jp.newpart.externalwarehouse.biz.domain.vo;


import lombok.Data;

/**
 * 問合せ対応履歴情報 詳細情報Vo
 *
 * @author nextosd
 */
@Data
public class BssAskHistoryVo {

    /**
     * Id
     */
    private String id;

    /**
     * ステータス
     */
    private String status;

    /**
     * ステータス
     */
    private String statusLabel;

    /**
     * カテゴリ（大）
     */
    private String category1;

    /**
     * カテゴリ（中）
     */
    private String category2;

    /**
     * お問い合わせ・対応内容
     */
    private String askAnswerRemark;

    /**
     * 対応者
     */
    private String answerUser;

    /**
     * 対応時間
     */
    private String answerTime;
}