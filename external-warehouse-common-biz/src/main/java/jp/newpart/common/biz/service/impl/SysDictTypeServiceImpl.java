package jp.newpart.common.biz.service.impl;

import jakarta.annotation.PostConstruct;
import jp.newpart.common.biz.mapper.SysDictDataMapper;
import jp.newpart.common.biz.mapper.SysDictTypeMapper;
import jp.newpart.common.biz.service.ISysDictTypeService;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.core.domain.entity.SysDictType;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.DictUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 辞書 ビジネス層処理
 *
 * @author nextosd
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * プロジェクト起動時、辞書をキャッシュに初期化する
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 条件に基づいてページングで辞書タイプを検索する
     *
     * @param dictType 辞書タイプ情報
     * @return 辞書タイプ集合情報
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 全ての辞書タイプを検索する
     *
     * @return 辞書タイプ集合情報
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 辞書タイプに基づいて辞書データを検索する
     *
     * @param dictType 辞書タイプ
     * @return 辞書データ集合情報
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 辞書タイプIDに基づいて情報を検索する
     *
     * @param dictId 辞書タイプID
     * @return 辞書タイプ
     */
    @Override
    public SysDictType selectDictTypeById(String dictId) {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 辞書タイプに基づいて情報を検索する
     *
     * @param dictType 辞書タイプ
     * @return 辞書タイプ
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 辞書タイプ情報を一括で削除する
     *
     * @param dictIds 削除する必要がある辞書ID
     */
    @Override
    public void deleteDictTypeByIds(String[] dictIds) {
        for (String dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$sは既に割り当てられているため、削除できません", dictType.getDictName()));
            }
            dictTypeMapper.deleteDictTypeById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 辞書キャッシュデータを読み込む
     */
    @Override
    public void loadingDictCache() {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 辞書キャッシュデータをクリアする
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 辞書キャッシュデータをリセットする
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新規保存辞書タイプ情報を追加する
     *
     * @param dict 辞書タイプ情報
     * @return 結果
     */
    @Override
    public int insertDictType(SysDictType dict) {
        dict.setDictId(IdUtils.nextIdStr());
        int row = dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 辞書タイプ情報を更新して保存する
     *
     * @param dict 辞書タイプ情報
     * @return 結果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = dictTypeMapper.selectDictTypeById(dict.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 辞書タイプが一意かどうかを確認する
     *
     * @param dict 辞書タイプ
     * @return 結果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        String dictId = dict.getDictId();
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && !dictType.getDictId().equals(dictId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}