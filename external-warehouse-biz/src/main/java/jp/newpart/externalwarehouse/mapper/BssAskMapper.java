package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.externalwarehouse.biz.domain.vo.BssAskVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 問合せ情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskMapper {

    /**
     * 注文番号による問い合わせ履歴を検索
     *
     * @param orderId 注文番号
     * @param farmerId 農家ID
     * @return 問い合わせ履歴情報
     */
    List<BssAskVo> selectAskListHistoryByOrderId(@Param("orderId")String orderId, @Param("farmerId") String farmerId);
}
