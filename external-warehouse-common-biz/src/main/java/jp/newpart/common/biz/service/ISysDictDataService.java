package jp.newpart.common.biz.service;

import java.util.List;
import jp.newpart.common.core.domain.entity.SysDictData;

/**
 * 辞書 ビジネス層
 *
 * @author nextosd
 */
public interface ISysDictDataService {

    /**
     * 条件に基づいてページ分割して辞書データを検索する
     *
     * @param dictData 辞書データ情報
     * @return 辞書データ集合情報
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 辞書タイプと辞書キー値に基づいて辞書データ情報を検索する
     *
     * @param dictType  辞書タイプ
     * @param dictValue 辞書キー値
     * @return 辞書ラベル
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 辞書データIDに基づいて情報を検索する
     *
     * @param dictCode 辞書データID
     * @return 辞書データ
     */
    public SysDictData selectDictDataById(String dictCode);

    /**
     * 辞書データ情報を一括で削除する
     *
     * @param dictCodes 削除する必要がある辞書データID
     */
    public void deleteDictDataByIds(String[] dictCodes);

    /**
     * 辞書データ情報を新規保存する
     *
     * @param dictData 辞書データ情報
     * @return 結果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 辞書データ情報を更新して保存する
     *
     * @param dictData 辞書データ情報
     * @return 結果
     */
    public int updateDictData(SysDictData dictData);
}