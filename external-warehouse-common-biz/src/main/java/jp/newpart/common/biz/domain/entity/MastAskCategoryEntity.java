package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 問合せカテゴリ (mast_ask_category)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastAskCategoryEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * カテゴリID
     */
    private String categoryId;

    /**
     * 親カテゴリID
     */
    private String parentCategoryId;

    /**
     * レベル
     */
    private Integer level;

    /**
     * カテゴリ名
     */
    private String categoryName;

    /**
     * メモ
     */
    private String memo;

    /**
     * 廃止フラグ
     */
    private String discontinuedFlg;

    /**
     * Version
     */
    private Integer version;

}
