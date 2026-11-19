package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serial;
import java.math.BigDecimal;
import jp.nasta.pms.common.annotation.EncryptedField;

/**
 * ロケーションマスタ (mast_location)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastLocationEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ロケーション情報ID
     */
    private String id;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 保管エリアコード
     */
    private String zoneCd;

    /**
     * ロケーションコード
     */
    private String locCd;

    /**
     * ロケーション名称
     */
    private String locNm;

    /**
     * ロケーションタイプ
     */
    private String locType;

    /**
     * 行
     */
    private String locRow;

    /**
     * 列
     */
    private String locCol;

    /**
     * 層
     */
    private String locDan;

    /**
     * 長さ
     */
    private String locWdt;

    /**
     * 幅
     */
    private String locVtc;

    /**
     * 高さ
     */
    private String locHgt;

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
