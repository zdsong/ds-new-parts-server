package jp.newpart.externalwarehouse.biz.domain.bo;

import jp.newpart.common.biz.enums.BssUserCertificationType;
import lombok.Data;

/**
 * 認証コード - EMail送信
 *
 * @author nextosd
 */
@Data
public class BssUserAuthCodeMailBo {

    /**
     * ユーザーID
     */
    private String userId;

    /**
     * ユーザー
     */
    private String userName;

    /**
     * メールアドレス
     */
    private String mailAddress;

    /**
     * 種類
     */
    private BssUserCertificationType type;

    /**
     * プログラムＩＤ
     */
    private String pgId;
}
