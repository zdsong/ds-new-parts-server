package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品写真マスタ (mast_item_image)
 *
 * @author nextosd
 */
@Data
public class MastItemImageVo implements Serializable {

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
}
