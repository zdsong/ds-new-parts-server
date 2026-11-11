package jp.newpart.common.enums;

/**
 * ビジネス・オペレーション・タイプ
 * 
 * @author nextosd
 */
public enum BusinessType
{
    /**
     * その他
     */
    OTHER,

    /**
     * Insert
     */
    INSERT,

    /**
     * Update
     */
    UPDATE,

    /**
     * Save
     */
    SAVE,

    /**
     * Delete
     */
    DELETE,

    /**
     * 授権
     */
    GRANT,

    /**
     * Export
     */
    EXPORT,

    /**
     * Import
     */
    IMPORT,

    /**
     * 強退
     */
    FORCE,

    /**
     * コードの生成
     */
    GENCODE,
    
    /**
     * データの消去
     */
    CLEAN,
}
