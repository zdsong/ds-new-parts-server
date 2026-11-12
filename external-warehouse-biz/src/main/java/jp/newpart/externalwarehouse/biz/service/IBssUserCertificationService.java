package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.biz.domain.entity.BssUserCertificationEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.BssUserAuthCodeMailBo;

/**
 * ユーザー認証情報Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssUserCertificationService {

    /**
     * ユーザIDに認証コードを取得する
     * @param userId
     * @return 認証情報
     */
    BssUserCertificationEntity getAuthCodeByUserId(String userId);

    /**
     * 認証コード - EMail送信する
     *
     * @param params 送信
     * @return ID
     */
    String sendAuthCodeByEmail(BssUserAuthCodeMailBo params);

    /**
     * IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
     int deleteLogicById(BssUserCertificationEntity bssUserCertification);
}
