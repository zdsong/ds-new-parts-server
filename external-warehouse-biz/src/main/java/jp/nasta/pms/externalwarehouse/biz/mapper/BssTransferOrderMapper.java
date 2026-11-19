package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferOpenLineVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 移動依頼 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssTransferOrderMapper {

    /**
     * パレットの移動依頼中明細を取得する
     *
     * @param palletNo パレット番号
     * @param status   移動状況
     * @return 移動依頼明細
     */
    List<BssTransferOpenLineVo> selectOpenByPalletNo(@Param("palletNo") String palletNo,
                                                     @Param("status") String status);
}
