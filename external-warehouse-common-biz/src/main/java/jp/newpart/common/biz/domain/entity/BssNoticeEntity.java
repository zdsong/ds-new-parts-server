package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * お知らせ (bss_notice)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssNoticeEntity extends BaseEntity {

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

    /**
     * 廃止フラグ
     */
    private String discontinuedFlg;

    /**
     * Version
     */
    private Integer version;

}
