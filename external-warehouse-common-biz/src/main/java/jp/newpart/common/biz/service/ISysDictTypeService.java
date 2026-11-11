package jp.newpart.common.biz.service;

import java.util.List;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.core.domain.entity.SysDictType;

/**
 * 辞書 ビジネス層
 *
 * @author nextosd
 */
public interface ISysDictTypeService {

    /**
     * 条件に基づいてページ分割して辞書タイプを検索する
     *
     * @param dictType 辞書タイプ情報
     * @return 辞書タイプ集合情報
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * すべての辞書タイプを検索する
     *
     * @return 辞書タイプ集合情報
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 辞書タイプに基づいて辞書データを検索する
     *
     * @param dictType 辞書タイプ
     * @return 辞書データ集合情報
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 辞書タイプIDに基づいて情報を検索する
     *
     * @param dictId 辞書タイプID
     * @return 辞書タイプ
     */
    public SysDictType selectDictTypeById(String dictId);

    /**
     * 辞書タイプに基づいて情報を検索する
     *
     * @param dictType 辞書タイプ
     * @return 辞書タイプ
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 辞書情報を一括で削除する
     *
     * @param dictIds 削除する必要がある辞書ID
     */
    public void deleteDictTypeByIds(String[] dictIds);

    /**
     * 辞書キャッシュデータを読み込む
     */
    public void loadingDictCache();

    /**
     * 辞書キャッシュデータをクリアする
     */
    public void clearDictCache();

    /**
     * 辞書キャッシュデータをリセットする
     */
    public void resetDictCache();

    /**
     * 辞書タイプ情報を新規保存する
     *
     * @param dictType 辞書タイプ情報
     * @return 結果
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 辞書タイプ情報を更新して保存する
     *
     * @param dictType 辞書タイプ情報
     * @return 結果
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 辞書タイプ名が一意であるかどうかを確認する
     *
     * @param dictType 辞書タイプ
     * @return 結果
     */
    public boolean checkDictTypeUnique(SysDictType dictType);
}