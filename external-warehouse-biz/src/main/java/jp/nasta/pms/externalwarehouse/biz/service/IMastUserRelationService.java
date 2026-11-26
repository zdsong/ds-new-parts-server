package jp.nasta.pms.externalwarehouse.biz.service;

import jp.nasta.pms.common.biz.domain.vo.OptionsKeyValueVo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationBatchSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationVo;

import java.util.List;

/**
 * ユーザー倉庫関係 Service
 *
 * @author nextosd
 */
public interface IMastUserRelationService {

    /**
     * ユーザーと倉庫の組み合わせで関係を検索する
     *
     * @param searchBo 検索条件
     * @return 一覧
     */
    List<MastUserRelationVo> selectList(MastUserRelationSearchBo searchBo);

    /**
     * ユーザー選択用の一覧を取得する
     *
     * @return ユーザー選択肢
     */
    List<OptionsKeyValueVo> selectUserOptions();

    /**
     * 保管倉庫の選択リストを取得する
     *
     * @return 倉庫選択肢
     */
    List<OptionsKeyValueVo> selectWarehouseOptions();

    /**
     * 関係タイプを保存する。未選択のタイプは論理削除する
     *
     * @param saveBo 保存内容
     */
    void save(MastUserRelationSaveBo saveBo);

    /**
     * 複数のユーザー・倉庫へ同じ関係タイプを設定する
     *
     * @param batchBo 一括設定内容
     */
    void saveBatch(MastUserRelationBatchSaveBo batchBo);

    /**
     * ユーザーと倉庫の関係タイプをすべて論理削除する
     *
     * @param targets 削除対象
     */
    void delete(List<MastUserRelationSaveBo> targets);
}
