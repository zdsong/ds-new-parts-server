package jp.newpart.common.core.domain;

import lombok.Data;

/**
 * インポートのエラー情報VO
 *
 * @author nextosd
 */
@Data
public class ImportFileErrorVo {
    /**
     * エラー対象（ファイル名或はシート名）
     */
    private String objectName;
    /**
     * エラー行
     */
    private String rowLine;
    /**
     * エラーメッセージ
     */
    private String message;
}
