package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 移動管理 (bss_transfer)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssTransferEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 移動管理情報ID
     */
    private String id;

    /**
     * 移動番号
     */
    private String transferNo;

    /**
     * 移動状況
     */
    private String status;

    /**
     * 移動元倉庫コード
     */
    private String transferFmWhCd;

    /**
     * 移動先倉庫コード
     */
    private String transferToWhCd;

    /**
     * 移動先ロケコード
     */
    private String transferToLocCd;

    /**
     * 移動登録者
     */
    private String transferRegisteredBy;

    /**
     * 移動登録日時
     */
    private String transferRegisteredAt;

    /**
     * 移動完了者
     */
    private String completedBy;

    /**
     * 移動完了日時
     */
    private String completedAt;

    /**
     * 取消者
     */
    private String canceledBy;

    /**
     * 取消日時
     */
    private String canceledAt;

    /**
     * 備考
     */
    private String memo;

    /**
     * 移動完了備考
     */
    private String completionMemo;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    /**
     * Version
     */
    private Integer version;
}
