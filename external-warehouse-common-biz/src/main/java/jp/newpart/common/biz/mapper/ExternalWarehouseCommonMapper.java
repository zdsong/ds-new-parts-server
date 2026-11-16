package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.MastItemEntity;
import jp.newpart.common.biz.domain.entity.MastWarehouseEntity;

import java.util.List;
import java.util.Map;


/**
 * 共通のサービス Mapperインタフェース
 *
 * @author nextosd
 */
public interface ExternalWarehouseCommonMapper {

     /**
     * 倉庫共通データの取得
     *
     * @param userName 紐付け種別
     * @return 商品マスタリスト
     */
    public List<MastWarehouseEntity> selectMstWarehouseList(String userName);

     /**
     * 倉庫ロケーション共通データの取得
     *
     * @param whCd 倉庫コード
     * @return ロケーションコンボボックスリスト
     */
    public List<Map<String, Object>> selectLocationOptionsList(String whCd);
}
