package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import jp.nasta.pms.common.biz.domain.entity.MastLocationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * ロケーションマスタ 表示用情報 (mast_location)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastLocationVo extends MastLocationEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 倉庫名称
     */
    private String whNm;
}
