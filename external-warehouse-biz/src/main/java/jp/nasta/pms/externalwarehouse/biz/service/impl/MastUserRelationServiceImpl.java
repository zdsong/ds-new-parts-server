package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.domain.entity.MastUserRelationsEntity;
import jp.nasta.pms.common.biz.domain.entity.MastWarehouseEntity;
import jp.nasta.pms.common.biz.domain.vo.OptionsKeyValueVo;
import jp.nasta.pms.common.biz.service.ISysDictTypeService;
import jp.nasta.pms.common.core.domain.entity.SysDictData;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationBatchSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationRowVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationTypeVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.MastUserRelationMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IMastUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ユーザー倉庫関係 Service業務処理
 *
 * @author nextosd
 */
@Service
public class MastUserRelationServiceImpl implements IMastUserRelationService {

    /**
     * 関係タイプの辞書
     */
    private static final String RELATION_DICT_TYPE = "bss_user_relation_type";

    /**
     * 画面ID
     */
    private static final String PG_ID = "P604";

    /**
     * 倉庫名を関係先名称へ保存するときの最大長
     */
    private static final int MAPPING_NAME_MAX = 40;

    @Autowired
    private MastUserRelationMapper mastUserRelationMapper;

    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * ユーザーと倉庫の組み合わせで関係を検索する
     *
     * @param searchBo 検索条件
     * @return 一覧
     */
    @Override
    public List<MastUserRelationVo> selectList(MastUserRelationSearchBo searchBo) {
        List<MastUserRelationRowVo> rows = mastUserRelationMapper.selectRows(searchBo);
        Map<String, MastUserRelationVo> grouped = new LinkedHashMap<>();
        for (MastUserRelationRowVo row : rows) {
            String key = row.getUserName() + "|" + row.getWhCd();
            MastUserRelationVo vo = grouped.get(key);
            if (vo == null) {
                vo = new MastUserRelationVo();
                vo.setId(key);
                vo.setUserName(row.getUserName());
                vo.setNickName(StringUtils.isNotEmpty(row.getNickName()) ? row.getNickName() : row.getUserName());
                vo.setWhCd(row.getWhCd());
                vo.setWhNm(row.getWhNm());
                vo.setMemo(row.getMemo());
                grouped.put(key, vo);
            } else if (StringUtils.isEmpty(vo.getMemo()) && StringUtils.isNotEmpty(row.getMemo())) {
                vo.setMemo(row.getMemo());
            }
            vo.getRelationTypes().add(row.getMappingType());
            MastUserRelationTypeVo typeVo = new MastUserRelationTypeVo();
            typeVo.setId(row.getId());
            typeVo.setMappingType(row.getMappingType());
            typeVo.setVersion(row.getVersion());
            vo.getTypeRows().add(typeVo);
        }

        List<MastUserRelationVo> result = new ArrayList<>(grouped.values());
        Set<String> typeFilter = new HashSet<>(splitTypes(searchBo == null ? null : searchBo.getRelationType()));
        if (!typeFilter.isEmpty()) {
            // 指定した関係タイプを1つでも持つ組み合わせだけ残す
            result.removeIf(vo -> vo.getRelationTypes().stream().noneMatch(typeFilter::contains));
        }
        return result;
    }

    /**
     * ユーザー選択用の一覧を取得する
     *
     * @return ユーザー選択肢
     */
    @Override
    public List<OptionsKeyValueVo> selectUserOptions() {
        return mastUserRelationMapper.selectUsers().stream()
                .map(row -> OptionsKeyValueVo.builder()
                        .key(row.getUserName())
                        .value(StringUtils.isNotEmpty(row.getNickName()) ? row.getNickName() : row.getUserName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 保管倉庫の選択リストを取得する
     *
     * @return 倉庫選択肢
     */
    @Override
    public List<OptionsKeyValueVo> selectWarehouseOptions() {
        return mastUserRelationMapper.selectWarehouses().stream()
                .map(row -> OptionsKeyValueVo.builder()
                        .key(row.getWhCd())
                        .value(StringUtils.isNotEmpty(row.getWhNm()) ? row.getWhNm() : row.getWhCd())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 関係タイプを保存する
     *
     * @param saveBo 保存内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MastUserRelationSaveBo saveBo) {
        saveOne(saveBo);
    }

    /**
     * 複数のユーザー・倉庫へ同じ関係タイプを設定する
     *
     * @param batchBo 一括設定内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(MastUserRelationBatchSaveBo batchBo) {
        if (batchBo == null || batchBo.getTargets() == null || batchBo.getTargets().isEmpty()) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.userWarehouseRelation")));
        }
        List<String> relationTypes = normalizeTypes(batchBo.getRelationTypes());
        if (relationTypes.isEmpty()) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.relationType")));
        }
        for (MastUserRelationSaveBo target : batchBo.getTargets()) {
            target.setRelationTypes(relationTypes);
            saveOne(target);
        }
    }

    /**
     * ユーザーと倉庫の関係タイプをすべて論理削除する
     *
     * @param targets 削除対象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<MastUserRelationSaveBo> targets) {
        if (targets == null || targets.isEmpty()) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.userWarehouseRelation")));
        }
        Date now = new Date();
        String username = SecurityUtils.getUsername();
        for (MastUserRelationSaveBo target : targets) {
            String userName = trim(target.getUserName());
            String whCd = trim(target.getWhCd());
            List<MastUserRelationRowVo> current = mastUserRelationMapper.selectActiveByUserAndWh(userName, whCd);
            assertNotUpdated(target.getTypeRows(), current);
            if (current.isEmpty()) {
                throw new ServiceException(MessageUtils.message("EM009"));
            }
            // 表示時点の関係タイプをすべて論理削除する
            for (MastUserRelationRowVo row : current) {
                logicalDelete(row, now, username);
            }
        }
    }

    /**
     * 1組のユーザーと倉庫について関係タイプを揃える
     *
     * @param saveBo 保存内容
     */
    private void saveOne(MastUserRelationSaveBo saveBo) {
        if (saveBo == null) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.userName")));
        }
        String userName = trim(saveBo.getUserName());
        String whCd = trim(saveBo.getWhCd());
        if (StringUtils.isEmpty(userName)) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.userName")));
        }
        if (StringUtils.isEmpty(whCd)) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.warehouse")));
        }
        if (mastUserRelationMapper.countActiveUser(userName) == 0) {
            throw new ServiceException(MessageUtils.message("E0016", MessageUtils.message("label.userName")));
        }
        MastWarehouseEntity warehouse = mastUserRelationMapper.selectWarehouse(whCd);
        if (warehouse == null) {
            throw new ServiceException(MessageUtils.message("E0016", MessageUtils.message("label.warehouse")));
        }

        List<String> relationTypes = normalizeTypes(saveBo.getRelationTypes());
        if (relationTypes.isEmpty()) {
            throw new ServiceException(MessageUtils.message("E0023", MessageUtils.message("label.relationType")));
        }
        Set<String> allowed = loadAllowedTypes();
        for (String relationType : relationTypes) {
            if (!allowed.contains(relationType)) {
                throw new ServiceException(MessageUtils.message("E0016", MessageUtils.message("label.relationType")));
            }
        }

        List<MastUserRelationRowVo> current = mastUserRelationMapper.selectActiveByUserAndWh(userName, whCd);
        List<MastUserRelationTypeVo> known = saveBo.getTypeRows() == null ? List.of() : saveBo.getTypeRows();
        if (known.isEmpty() && !current.isEmpty()) {
            // 同じユーザーと倉庫の関係が既にある場合は新規登録しない
            throw new ServiceException(MessageUtils.message("E0073"));
        }
        assertNotUpdated(known, current);

        Set<String> wanted = new HashSet<>(relationTypes);
        Set<String> existingTypes = current.stream()
                .map(MastUserRelationRowVo::getMappingType)
                .collect(Collectors.toSet());
        String memo = current.stream()
                .map(MastUserRelationRowVo::getMemo)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElse(null);

        Date now = new Date();
        String username = SecurityUtils.getUsername();
        String mappingName = shorten(warehouse.getWhNm(), MAPPING_NAME_MAX);

        // 新しく選ばれた関係タイプを追加する
        for (String relationType : relationTypes) {
            if (existingTypes.contains(relationType)) {
                continue;
            }
            MastUserRelationsEntity entity = new MastUserRelationsEntity();
            entity.setId(IdUtils.nextIdStr());
            entity.setUserName(userName);
            entity.setMappingType(relationType);
            entity.setMappingId(whCd);
            entity.setMappingName(mappingName);
            entity.setMemo(memo);
            entity.setDeleteFlg("0");
            entity.setVersion(1);
            entity.setCreateTime(now);
            entity.setCreateBy(username);
            entity.setCreatePgId(PG_ID);
            entity.setUpdateTime(now);
            entity.setUpdateBy(username);
            entity.setUpdatePgId(PG_ID);
            mastUserRelationMapper.insert(entity);
        }

        // 外された関係タイプを論理削除する
        for (MastUserRelationRowVo row : current) {
            if (!wanted.contains(row.getMappingType())) {
                logicalDelete(row, now, username);
            }
        }
    }

    /**
     * 画面表示後に他ユーザーが更新していないことを確認する
     *
     * @param known   画面が保持している関係タイプ
     * @param current 現在の関係タイプ
     */
    private void assertNotUpdated(List<MastUserRelationTypeVo> known, List<MastUserRelationRowVo> current) {
        List<MastUserRelationTypeVo> clientRows = known == null ? List.of() : known;
        if (clientRows.isEmpty()) {
            return;
        }
        if (clientRows.size() != current.size()) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
        Map<String, MastUserRelationRowVo> byType = current.stream()
                .collect(Collectors.toMap(MastUserRelationRowVo::getMappingType, row -> row, (left, right) -> left));
        for (MastUserRelationTypeVo clientRow : clientRows) {
            MastUserRelationRowVo currentRow = byType.get(clientRow.getMappingType());
            if (currentRow == null
                    || !Objects.equals(currentRow.getId(), clientRow.getId())
                    || !Objects.equals(currentRow.getVersion(), clientRow.getVersion())) {
                throw new ServiceException(MessageUtils.message("E0007"));
            }
        }
    }

    /**
     * 関係を論理削除する
     *
     * @param row      削除する行
     * @param now      更新日時
     * @param username 更新者
     */
    private void logicalDelete(MastUserRelationRowVo row, Date now, String username) {
        MastUserRelationsEntity entity = new MastUserRelationsEntity();
        entity.setId(row.getId());
        entity.setVersion(row.getVersion());
        entity.setUpdateTime(now);
        entity.setUpdateBy(username);
        entity.setUpdatePgId(PG_ID);
        int updated = mastUserRelationMapper.deleteLogical(entity);
        if (updated == 0) {
            throw new ServiceException(MessageUtils.message("E0007"));
        }
    }

    /**
     * 辞書に登録されている関係タイプを取得する
     *
     * @return 関係タイプの値
     */
    private Set<String> loadAllowedTypes() {
        List<SysDictData> dictData = dictTypeService.selectDictDataByType(RELATION_DICT_TYPE);
        if (dictData == null) {
            return Set.of();
        }
        return dictData.stream()
                .filter(item -> item != null && "0".equals(item.getStatus()))
                .map(SysDictData::getDictValue)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
    }

    /**
     * 関係タイプの重複と空白を除く
     *
     * @param relationTypes 関係タイプ
     * @return 正規化した関係タイプ
     */
    private List<String> normalizeTypes(List<String> relationTypes) {
        if (relationTypes == null) {
            return List.of();
        }
        List<String> normalized = new ArrayList<>();
        for (String relationType : relationTypes) {
            String value = trim(relationType);
            if (StringUtils.isNotEmpty(value) && !normalized.contains(value)) {
                normalized.add(value);
            }
        }
        return normalized;
    }

    /**
     * カンマ区切りの関係タイプを分解する
     *
     * @param relationType カンマ区切りの関係タイプ
     * @return 関係タイプ
     */
    private List<String> splitTypes(String relationType) {
        if (StringUtils.isEmpty(relationType)) {
            return List.of();
        }
        return Arrays.stream(relationType.split(","))
                .map(this::trim)
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 前後の空白を除く
     *
     * @param value 文字列
     * @return 空白を除いた文字列
     */
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * 指定長を超える文字を切り捨てる
     *
     * @param value 文字列
     * @param max   最大長
     * @return 切り捨て後の文字列
     */
    private String shorten(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }
}
