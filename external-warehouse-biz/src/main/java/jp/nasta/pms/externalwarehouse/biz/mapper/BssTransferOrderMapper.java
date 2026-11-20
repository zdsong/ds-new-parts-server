package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;
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

    /**
     * 移動依頼一覧を取得する
     *
     * @param searchBo 検索条件
     * @return 移動依頼一覧
     */
    List<BssTransferListVo> selectTransferList(BssTransferSearchBo searchBo);

    /**
     * 移動依頼番号の品目を取得する
     *
     * @param requestNos 移動依頼番号
     * @return 品目
     */
    List<BssTransferItemVo> selectItemsByRequestNos(@Param("requestNos") List<String> requestNos);
}
