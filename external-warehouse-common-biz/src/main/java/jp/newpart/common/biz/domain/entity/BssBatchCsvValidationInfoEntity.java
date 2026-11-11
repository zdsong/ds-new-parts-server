package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * バッチ実行CSV検証情報 (bss_batch_csv_validation_info)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssBatchCsvValidationInfoEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 検証情報ID
     */
    private String id;

    /**
     * CSVファイルの種類
     */
    private String csvFileType;

    /**
     * テーブル名
     */
    private String tableName;

    /**
     * エンティティ名
     */
    private String entityName;

    /**
     * フィールド名
     */
    private String fieldRequired;

    /**
     * フィールド名
     */
    private String fieldName;

    /**
     * フィールド説明
     */
    private String fieldDesc;

    /**
     * フィールド長
     */
    private String fieldLength;

    /**
     * フィールドの種類
     */
    private String fieldType;

    /**
     * フィールド検証形式
     */
    private String fieldFormat;

    /**
     * フィールドの検証カテゴリ
     */
    private String fieldCategory;

    /**
     * データ辞書名
     */
    private String dataDictName;

    /**
     * CSV Import フラグ
     */
    private String csvImportFlg;

    /**
     * CSV Export フラグ
     */
    private String csvExportFlg;

    /**
     * CSVファイルの列インデックス
     */
    private Integer csvFileColIdx;

    /**
     * CSVファイルの列名
     */
    private String csvFileColName;

    /**
     * Version
     */
    private Integer version;

}
