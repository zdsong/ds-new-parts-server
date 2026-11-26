package jp.nasta.pms.externalwarehouse.biz.mapper;

import jp.nasta.pms.common.biz.domain.entity.MastUserRelationsEntity;
import jp.nasta.pms.common.biz.domain.entity.MastWarehouseEntity;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationRowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ユーザー倉庫関係 Mapper
 *
 * @author nextosd
 */
public interface MastUserRelationMapper {

    /**
     * 辞書 bss_user_relation_type に属する有効な関係を検索する
     *
     * @param searchBo 検索条件
     * @return 関係タイプごとの行
     */
    List<MastUserRelationRowVo> selectRows(MastUserRelationSearchBo searchBo);

    /**
     * ユーザーと倉庫に紐づく有効な関係タイプを取得する
     *
     * @param userName ユーザーID
     * @param whCd     倉庫コード
     * @return 関係タイプごとの行
     */
    List<MastUserRelationRowVo> selectActiveByUserAndWh(@Param("userName") String userName,
                                                        @Param("whCd") String whCd);

    /**
     * 有効なユーザーを取得する
     *
     * @return ユーザー一覧
     */
    List<MastUserRelationRowVo> selectUsers();

    /**
     * 有効な倉庫を取得する
     *
     * @return 倉庫一覧
     */
    List<MastWarehouseEntity> selectWarehouses();

    /**
     * 倉庫を取得する
     *
     * @param whCd 倉庫コード
     * @return 倉庫。存在しない場合は null
     */
    MastWarehouseEntity selectWarehouse(@Param("whCd") String whCd);

    /**
     * 利用可能なユーザーが存在するか
     *
     * @param userName ユーザーID
     * @return 件数
     */
    int countActiveUser(@Param("userName") String userName);

    /**
     * 関係を登録する
     *
     * @param entity 関係
     * @return 件数
     */
    int insert(MastUserRelationsEntity entity);

    /**
     * 関係を論理削除する（楽観ロック）
     *
     * @param entity 関係
     * @return 件数
     */
    int deleteLogical(MastUserRelationsEntity entity);
}
