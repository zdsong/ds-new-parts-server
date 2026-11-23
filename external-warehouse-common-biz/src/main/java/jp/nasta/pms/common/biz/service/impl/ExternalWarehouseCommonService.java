package jp.nasta.pms.common.biz.service.impl;

import jp.nasta.pms.common.biz.domain.bo.OptionsParamBo;

import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import jp.nasta.pms.common.biz.domain.entity.MastWarehouseEntity;
import jp.nasta.pms.common.biz.domain.vo.OptionsKeyValueVo;

import jp.nasta.pms.common.biz.mapper.ExternalWarehouseCommonMapper;
import jp.nasta.pms.common.biz.mapper.SysDictDataMapper;
import jp.nasta.pms.common.biz.service.IExternalWarehouseCommonService;
import jp.nasta.pms.common.core.domain.entity.SysDictData;

import jp.nasta.pms.common.core.service.ISysRangedSeqService;
import jp.nasta.pms.common.enums.ClassificationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static jp.nasta.pms.common.constant.Constants.SERIAL_PREFIX_PALLET;
import static jp.nasta.pms.common.constant.Constants.SERIAL_PREFIX_PALLET_PART;

/**
 * 共通のサービスクラス
 *
 * @author nextosd
 */
@Service
public class ExternalWarehouseCommonService implements IExternalWarehouseCommonService {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private ExternalWarehouseCommonMapper ewCommonMapper;

    @Autowired
    private ISysRangedSeqService sysRangedSeqService;

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


    /**
     * コンボボックスデータ(倉庫)を取得する
     *
     * @param userName コンボボックスパラメータ
     * @return コンボボックスリスト
     */
    public List<OptionsKeyValueVo> getWarehouseCommonOptions(String userName) {
        List<MastWarehouseEntity> mastWarehouseList =  ewCommonMapper.selectMstWarehouseList(userName);
        return mastWarehouseList.stream()
                // 倉庫コード順に並べ替える（削除済みデータは SQL 側で除外済み）
                .sorted(Comparator.comparing(
                        MastWarehouseEntity::getWhCd,
                        Comparator.nullsFirst(String::compareTo)
                ))
                .map(e -> OptionsKeyValueVo.builder()
                        .key(e.getWhCd())
                        .value(e.getWhNm())

                        .build())
                .collect(Collectors.toList());
    }

    /**
     * コンボボックスデータ(倉庫ロケーション)を取得する
     *
     * @param whCd 倉庫コード
     * @return コンボボックスリスト
     */
    @Override
    public List<OptionsKeyValueVo> getLocationCommonOptions(String whCd) {
        return ewCommonMapper.selectLocationOptionsList(whCd).stream()
                .map(row -> OptionsKeyValueVo.builder()
                        .key(String.valueOf(row.get("key")))
                        .value(String.valueOf(row.get("value")))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * パレット番号の生成
     *
     * @param classification 区分（部品は B、製品は S を先頭に付ける）
     * @return パレット番号
     */
    public String generatePalletNo(String classification) {
        // 品目区分が部品の場合は B、それ以外（製品）は S を先頭に付ける
        String prefix = ClassificationEnum.PART.getCode().equals(classification) ? SERIAL_PREFIX_PALLET_PART : SERIAL_PREFIX_PALLET;
        return sysRangedSeqService.generateSerialNo(prefix);
    }
}
