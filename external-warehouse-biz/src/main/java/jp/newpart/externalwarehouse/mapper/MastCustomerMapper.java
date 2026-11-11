package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.externalwarehouse.biz.domain.bo.MastCustomerSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastCustomerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 顧客マスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastCustomerMapper {

    /**
     * 顧客マスタの検索
     *
     * @param id 顧客マスタプライマリ・キー
     * @return 顧客マスタ
     */
    public MastCustomerVo selectCustomerById(String id);

    /**
     * 顧客マスタの検索
     *
     * @param customerId 顧客ID
     * @param farmerId  農家ID
     * @return 顧客マスタ
     */
    public MastCustomerVo selectCustomerByCustomerId(@Param("customerId") String customerId, @Param("farmerId") String farmerId);

    /**
     * 顧客マスタを検索リスト
     *
     * @param mastCustomerSearchBo 顧客マスタ検索パラメータ
     * @return 顧客マスタリスト
     */
    public List<MastCustomerVo> selectList(MastCustomerSearchBo mastCustomerSearchBo);
}
