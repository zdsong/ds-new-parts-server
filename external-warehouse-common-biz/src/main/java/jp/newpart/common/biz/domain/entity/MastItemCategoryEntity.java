package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品カテゴリマスタ (mast_item_category)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastItemCategoryEntity extends BaseEntity {

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
     * カテゴリ種類
     */
    private String categoryType;

    /**
     * カテゴリ名
     */
    private String categoryName;

    /**
     * 収穫期間
     */
    private Integer harvestPeriod;

    /**
     * 要確認
     */
    private String confirmFlg;

    /**
     * 農家ID
     */
    private String farmerId;

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
