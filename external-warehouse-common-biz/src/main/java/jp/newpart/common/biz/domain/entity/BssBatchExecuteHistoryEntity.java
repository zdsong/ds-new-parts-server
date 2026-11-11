package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * スケジュール実行履歴 (bss_batch_execute_history)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssBatchExecuteHistoryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ジョブ実行履歴ID
     */
    private String id;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * ファイル名
     */
    private String fileName;

    /**
     * 実行ステータス
     */
    private String execStatus;

    /**
     * ダウンロードファイルパス
     */
    private String downloadPath;

    /**
     * アップロードファイルパス
     */
    private String uploadPath;

    /**
     * 実行時間
     */
    private String execTime;

    /**
     * メール送信フラグ
     */
    private String emailSendFlg;

    /**
     * 合計行数
     */
    private Integer totalLines;

    /**
     * 成功行数
     */
    private Integer successLines;

    /**
     * 失敗行数
     */
    private Integer failLines;

    /**
     * ジョブ実行キー
     */
    private String executeKey;

    /**
     * Version
     */
    private Integer version;

}
