package jp.nasta.pms.common.biz.mapper;

import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import jp.nasta.pms.common.biz.domain.entity.MastWarehouseEntity;
import org.apache.ibatis.annotations.Param;

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
     * 未削除の倉庫をすべて取得する
     *
     * @return 倉庫リスト
     */
    public List<MastWarehouseEntity> selectAllActiveWarehouses();

    /**
     * 指定した辞書タイプの関係がある倉庫を取得する
     *
     * @param userName ユーザー名
     * @param dictType 辞書タイプ
     * @return 倉庫リスト
     */
    public List<MastWarehouseEntity> selectWarehousesByRelationDict(@Param("userName") String userName,
                                                                     @Param("dictType") String dictType);

    /**
     * ユーザー種別を取得する
     *
     * @param userName ユーザー名
     * @return ユーザー種別
     */
    public String selectUserType(String userName);

     /**
     * 倉庫ロケーション共通データの取得
     *
     * @param whCd 倉庫コード
     * @return ロケーションコンボボックスリスト
     */
    public List<Map<String, Object>> selectLocationOptionsList(String whCd);
}
