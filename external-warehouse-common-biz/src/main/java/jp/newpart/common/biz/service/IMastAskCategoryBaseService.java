package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastAskCategoryEntity;

import java.util.List;


/**
 * 問合せカテゴリServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastAskCategoryBaseService {

    /**
     * 問合せカテゴリを検索
     *
     * @param id 問合せカテゴリプライマリ・キー
     * @return 問合せカテゴリ
     */
    public MastAskCategoryEntity selectById(String id);

    /**
     * 問合せカテゴリを検索リスト
     *
     * @param mastAskCategory 問合せカテゴリパラメータ対象
     * @return 問合せカテゴリリスト
     */
    public List<MastAskCategoryEntity> selectList(MastAskCategoryEntity mastAskCategory);

    /**
     * 問合せカテゴリを登録
     *
     * @param mastAskCategory 問合せカテゴリ
     * @return 結果
     */
    public int insert(MastAskCategoryEntity mastAskCategory);

    /**
     * 問合せカテゴリを修正
     *
     * @param mastAskCategory 問合せカテゴリ
     * @return 結果
     */
    public int update(MastAskCategoryEntity mastAskCategory);

    /**
     * 問合せカテゴリを削除
     *
     * @param id 問合せカテゴリプライマリ・キー
     * @return 結果
     */
    public int deleteById(String id);

}
