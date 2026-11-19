package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * パレット番号 表示用情報 (bss_pallet_number)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssPalletNumberVo extends BssPalletNumberEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 倉庫名称
     */
    private String whNm;

    /**
     * 積載明細
     */
    private List<BssPalletNumberDetailEntity> details;
}
