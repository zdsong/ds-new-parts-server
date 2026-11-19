package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.common.biz.domain.entity.BssInventoryDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssInventoryHistoryEntity;
import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在庫情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssInventoryMapper {

    /**
     * 在庫情報詳細を登録する
     *
     * @param entity 在庫情報詳細
     * @return 登録件数
     */
    int insertDetail(BssInventoryDetailEntity entity);

    /**
     * 在庫履歴を登録する
     *
     * @param entity 在庫履歴
     * @return 登録件数
     */
    int insertHistory(BssInventoryHistoryEntity entity);

    /**
     * 品目の単位と品目名を取得する
     *
     * @param itemCodes 品目コード
     * @return 品目
     */
    List<MastItemEntity> selectItemsByCodes(@Param("itemCodes") List<String> itemCodes);

    /**
     * ロケーションの保管エリアコードを取得する
     *
     * @param whCd  倉庫コード
     * @param locCd ロケコード
     * @return 保管エリアコード
     */
    String selectZoneCd(@Param("whCd") String whCd, @Param("locCd") String locCd);
}
