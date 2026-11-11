package jp.newpart.common.biz.mapper;

import java.util.List;
import jp.newpart.common.core.domain.entity.SysDictType;

/**
 * 辞書表 データ層
 *
 * @author nextosd
 */
public interface SysDictTypeMapper {

    /**
     * 条件に応じてページ分割して辞書タイプを查詢
     *
     * @param dictType 辞書タイプ情報
     * @return 辞書タイプ集合情報
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * すべての辞書タイプを查詢
     *
     * @return 辞書タイプ集合情報
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 辞書タイプIDで情報を查詢
     *
     * @param dictId 辞書タイプID
     * @return 辞書タイプ
     */
    public SysDictType selectDictTypeById(String dictId);

    /**
     * 辞書タイプで情報を查詢
     *
     * @param dictType 辞書タイプ
     * @return 辞書タイプ
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 辞書IDで辞書情報を削除
     *
     * @param dictId 辞書ID
     * @return 結果
     */
    public int deleteDictTypeById(String dictId);

    /**
     * 辞書タイプ情報を一括削除
     *
     * @param dictIds 削除する辞書ID
     * @return 結果
     */
    public int deleteDictTypeByIds(String[] dictIds);

    /**
     * 辞書タイプ情報を追加
     *
     * @param dictType 辞書タイプ情報
     * @return 結果
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 辞書タイプ情報を更新
     *
     * @param dictType 辞書タイプ情報
     * @return 結果
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 辞書タイプ名が一意であるかを検証
     *
     * @param dictType 辞書タイプ
     * @return 結果
     */
    public SysDictType checkDictTypeUnique(String dictType);
}
