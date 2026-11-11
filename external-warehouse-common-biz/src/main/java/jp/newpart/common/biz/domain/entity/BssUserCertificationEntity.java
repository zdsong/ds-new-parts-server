package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * ユーザー認証情報 (bss_user_certification)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssUserCertificationEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 認証方法 0:SMS 1:メール
     */
    private String certificationFlg;

    /**
     * メールアドレス 認証方法= 1 の場合、空にすることはできません。
     */
    private String mailAddress;

    /**
     * 電話番号 認証方法= 0 の場合、空にすることはできません。
     */
    private String phoneNumber;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 種類 0:CRM 1:農家
     */
    private String type;

    /**
     * 開始時間
     */
    private String startTime;

    /**
     * 終了時間
     */
    private String endTime;

    /**
     * 認証コード
     */
    private String certificationCode;

    /**
     * 廃止フラグ
     */
    private String discontinuedFlg;

    /**
     * リビジョン
     */
    private Integer version;
}
