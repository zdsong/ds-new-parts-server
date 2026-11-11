package jp.newpart.common.core.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 農家マスタ
 */
@Data
public class FarmerUserInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 農家ID
     */
    private String farmerId;

    /**
     * 農家名
     */
    private String farmerName;

    /**
     * 農家名（カナ）
     */
    private String farmerNameKn;

    /**
     * 電話番号
     */
    private String phoneNumber;

    /**
     * メールアドレス
     */
    private String mailAddress;

    /**
     * ログイン回数
     */
    private Integer loginCnt;

    /**
     * パスワード
     */
    private String userPassword;

    /**
     * 利用状態
     */
    private String usageStatus;

    /**
     * リビジョン
     */
    private Integer version;
}
