package jp.newpart.common.biz.domain.entity;

import jp.newpart.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品写真マスタ (mast_item_image)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastItemImageEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 画像URL
     */
    private String imageUrl;

    /**
     * 他画像URL
     */
    private String imageOtherUrl;

    /**
     * 一覧用表示画像URL
     */
    private String imageListUrl;

    /**
     * 廃止日
     */
    private String discontinuedDate;

    /**
     * リビジョン
     */
    private Integer version;
}
