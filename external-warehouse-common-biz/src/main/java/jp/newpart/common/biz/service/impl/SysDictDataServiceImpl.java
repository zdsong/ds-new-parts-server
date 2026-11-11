package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.mapper.SysDictDataMapper;
import jp.newpart.common.biz.service.ISysDictDataService;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.utils.DictUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 辞書 ビジネス層処理
 *
 * @author nextosd
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 条件に基づいてページングで辞書データを検索する
     *
     * @param dictData 辞書データ情報
     * @return 辞書データ集合情報
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 辞書タイプと辞書キー値に基づいて辞書データ情報を検索する
     *
     * @param dictType  辞書タイプ
     * @param dictValue 辞書キー値
     * @return 辞書ラベル
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 辞書データIDに基づいて情報を検索する
     *
     * @param dictCode 辞書データID
     * @return 辞書データ
     */
    @Override
    public SysDictData selectDictDataById(String dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 辞書データ情報を一括で削除する
     *
     * @param dictCodes 削除する必要がある辞書データID
     */
    @Override
    public void deleteDictDataByIds(String[] dictCodes) {
        for (String dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新規保存辞書データ情報を追加する
     *
     * @param data 辞書データ情報
     * @return 結果
     */
    @Override
    public int insertDictData(SysDictData data) {
        data.setDictCode(IdUtils.nextIdStr());
        int row = dictDataMapper.insertDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 辞書データ情報を更新して保存する
     *
     * @param data 辞書データ情報
     * @return 結果
     */
    @Override
    public int updateDictData(SysDictData data) {
        int row = dictDataMapper.updateDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
