package jp.newpart.common.biz.domain.bo;

import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.validator.annotation.LmNotBlank;
import jp.newpart.common.validator.utils.ValidatorGroup;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品カテゴリマスタ サービスパラメータ対象
 *
 * @author nextosd
 */
@Data
public class MastItemCategoryBaseBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @LmNotBlank(params = {"{id}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 20, params = {"{id}", "20"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String id;

    /**
     * カテゴリID
     */
    @LmNotBlank(params = {"{categoryId}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 20, params = {"{categoryId}", "20"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String categoryId;

    /**
     * 親カテゴリID
     */
    @LmNotBlank(params = {"{parentCategoryId}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 20, params = {"{parentCategoryId}", "20"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String parentCategoryId;

    /**
     * カテゴリ種類
     */
    @LmNotBlank(params = {"{categoryType}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 1, params = {"{categoryType}", "1"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String categoryType;

    /**
     * カテゴリ名
     */
    @LmNotBlank(params = {"{categoryName}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 32, params = {"{categoryName}", "32"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String categoryName;

    /**
     * 収穫期間
     */
    private Integer harvestPeriod;

    /**
     * 要確認
     */
    @LmLength(min = 1, max = 1, params = {"{confirmFlg}", "1"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String confirmFlg;

    /**
     * メモ
     */
    @LmLength(min = 1, max = 255, params = {"{memo}", "255"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String memo;

    /**
     * 廃止フラグ
     */
    @LmNotBlank(params = {"{discontinuedFlg}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 20, params = {"{discontinuedFlg}", "20"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String discontinuedFlg;

    /**
     * Version
     */
    private Integer version;
}
