package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.bo.OptionsParamBo;

import jp.newpart.common.biz.domain.vo.OptionsKeyValueVo;

import jp.newpart.common.biz.mapper.SysDictDataMapper;
import jp.newpart.common.biz.service.IExternalWarehouseCommonService;
import jp.newpart.common.core.domain.entity.SysDictData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 共通ロジックのサービスクラス
 *
 * @author nextosd
 */
@Service
public class ExternalWarehouseCommonService implements IExternalWarehouseCommonService {

    @Autowired
    private SysDictDataMapper dictDataMapper;
    /**
     * コンボボックスデータを取得する
     *
     * @param optionsParam コンボボックスパラメータ
     * @return コンボボックスリスト
     */
    @Override
    public List<OptionsKeyValueVo> getCommonOptions(OptionsParamBo optionsParam) {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        dictData.setDictType(optionsParam.getCmdOptionName());

        List<SysDictData> list = dictDataMapper.selectDictDataList(dictData);

        // 表示順・昇順
        Comparator<SysDictData> sortDisplayOrder = Comparator.nullsFirst(Comparator.comparing((SysDictData entity) -> 0, Comparator.nullsFirst(Integer::compareTo)));

        // ・昇順
        return list.stream()
                .sorted(sortDisplayOrder
                        .thenComparing(SysDictData::getDictSort))
                .map(item -> OptionsKeyValueVo.builder()
                        .key(item.getDictValue())
                        .value(item.getDictLabel())
                        .isDefault(item.getIsDefault()).build())
                .collect(Collectors.toList());
    }

}
