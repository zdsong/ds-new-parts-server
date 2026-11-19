package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.nasta.pms.common.annotation.EncryptedField;

/**
 * 倉庫マスタ (mast_warehouse)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastWarehouseEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 倉庫情報ID
     */
    private String id;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 倉庫名称
     */
    private String whNm;

    /**
     * 倉庫略称
     */
    private String whAlias;

    /**
     * 倉庫タイプ
     */
    private String whType;

    /**
     * アドレス
     */
    private String address;

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
