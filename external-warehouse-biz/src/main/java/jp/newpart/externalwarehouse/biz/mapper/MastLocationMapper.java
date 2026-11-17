package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.biz.domain.entity.MastLocationEntity;
import jp.newpart.common.biz.domain.entity.MastWarehouseEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.MastLocationSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastLocationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ロケーションマスタ Mapperインタフェース
 *
 * @author nextosd
 */
public interface MastLocationMapper {

    /**
     * ロケーションマスタを検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return ロケーションマスタリスト
     */
    List<MastLocationVo> selectList(MastLocationSearchBo searchBo);

    /**
     * ロケーションマスタを検索
     *
     * @param id ロケーションマスタプライマリ・キー
     * @return ロケーションマスタ
     */
    MastLocationVo selectById(@Param("id") String id);

    /**
     * 同一の倉庫コード・ロケーションコードが存在するか確認する
     *
     * @param whCd      倉庫コード
     * @param locCd     ロケーションコード
     * @param excludeId 除外するロケーションマスタID（更新時）
     * @return 件数
     */
    int countByWhCdAndLocCd(@Param("whCd") String whCd,
                            @Param("locCd") String locCd,
                            @Param("excludeId") String excludeId);

    /**
     * ロケーションマスタを登録
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int insert(MastLocationEntity entity);

    /**
     * ロケーションマスタを修正
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int update(MastLocationEntity entity);

    /**
     * ロケーションマスタを論理削除
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int deleteLogical(MastLocationEntity entity);

    /**
     * 倉庫名から倉庫情報を取得する
     *
     * @param whNm 倉庫名
     * @return 倉庫情報
     */
    MastWarehouseEntity selectWarehouseByNm(@Param("whNm") String whNm);
}
