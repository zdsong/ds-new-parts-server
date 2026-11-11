package jp.newpart.common.biz.mapper;

import java.util.List;

import jp.newpart.common.core.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

/**
 * 辞書テーブル データ層
 *
 * @author nextosd
 */
public interface SysDictDataMapper {

    /**
     * 条件に応じてページ分割して辞書データを查詢
     *
     * @param dictData 辞書データ情報
     * @return 辞書データ集合情報
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 辞書タイプで辞書データを查詢
     *
     * @param dictType 辞書タイプ
     * @return 辞書データ集合情報
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 辞書タイプと辞書キー値で辞書データ情報を查詢
     *
     * @param dictType  辞書タイプ
     * @param dictValue 辞書キー値
     * @return 辞書ラベル
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 辞書データIDで情報を查詢
     *
     * @param dictCode 辞書データID
     * @return 辞書データ
     */
    public SysDictData selectDictDataById(String dictCode);

    /**
     * 辞書タイプで辞書データを查詢
     *
     * @param dictType 辞書タイプ
     * @return 辞書データ
     */
    public int countDictDataByType(String dictType);

    /**
     * 辞書IDで辞書データ情報を削除
     *
     * @param dictCode 辞書データID
     * @return 結果
     */
    public int deleteDictDataById(String dictCode);

    /**
     * 辞書データ情報を一括削除
     *
     * @param dictCodes 削除する辞書データID
     * @return 結果
     */
    public int deleteDictDataByIds(String[] dictCodes);

    /**
     * 辞書データ情報を追加
     *
     * @param dictData 辞書データ情報
     * @return 結果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 辞書データ情報を更新
     *
     * @param dictData 辞書データ情報
     * @return 結果
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 辞書タイプを同期して更新
     *
     * @param oldDictType 旧辞書タイプ
     * @param newDictType 新辞書タイプ
     * @return 結果
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);

    /**
     * 辞書タイプで辞書データを查詢
     *
     * @param dictType 辞書タイプ
     * @return 辞書データ集合情報
     */
    public List<String> selectOnlyDictValue(String dictType);
}
