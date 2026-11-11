package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * お知らせ 検索パラメータ対象
 *
 * @author nextosd
 */
@Data
public class BssNoticeSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * お知ら種類
     */
    private String noticeType;

    /**
     * 遷移パラメータ
     */
    private String noticeParams;

    /**
     * お知らせ内容
     */
    private String noticeContent;

    /**
     * お知らせ者
     */
    private String noticeUser;

    /**
     * お知らせ時間
     */
    private String noticeTime;

    /**
     * ステータス
     */
    private String status;

    /**
     * 受信者
     */
    private String readUser;

    /**
     * 受信日時
     */
    private String readTime;

    /**
     * 受信者種類
     */
    private String readType;
}
