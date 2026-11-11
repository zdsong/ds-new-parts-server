package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 問合せ対応履歴情報  (bss_ask_history)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssAskHistoryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 問合せ番号
     */
    private String askId;

    /**
     * ステータス
     */
    private String status;

    /**
     * カテゴリ（大）
     */
    private String category1;

    /**
     * カテゴリ（中）
     */
    private String category2;

    /**
     * シリアルナンバー
     */
    private Integer seqNo;

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

    /**
     * リビジョン
     */
    private Integer version;
}
