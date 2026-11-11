package jp.newpart.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 画面表示画像
 *
 * @author nextosd
 */
@Data
public class ImageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 説明
     */
    private String desc;

    /**
     * 画像URL
     */
    private String imageUrl;

}
