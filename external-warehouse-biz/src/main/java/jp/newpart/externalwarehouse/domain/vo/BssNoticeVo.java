package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

/**
 * お知らせ情報Vo
 *
 * @author nextosd
 */
@Data
public class BssNoticeVo {

    /**
     * ID
     */
    private String id;

    /**
     * お知らせ者
     */
    private String noticeUser;

    /**
     * ステータス
     */
    private String status;

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
     * お知らせ時間
     */
    private String noticeTime;

    /**
     * 遷移URL
     */
    private String linkUrl;
}
