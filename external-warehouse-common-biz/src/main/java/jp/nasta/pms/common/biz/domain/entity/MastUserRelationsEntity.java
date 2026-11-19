package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.nasta.pms.common.annotation.EncryptedField;

/**
 * ユーザーと倉庫の関係 (mast_user_relations)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastUserRelationsEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * ユーザID
     */
    private String userName;

    /**
     * 紐付け種別
     */
    private String mappingType;

    /**
     * 紐付け先ID
     */
    private String mappingId;

    /**
     * 紐付け先名称
     */
    private String mappingName;

    /**
     * 順位
     */
    private String seq;

    /**
     * 備考
     */
    private String memo;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    /**
     * Version
     */
    private Integer version;

}
