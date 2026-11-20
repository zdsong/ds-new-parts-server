package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.domain.entity.BssInventoryDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssInventoryHistoryEntity;
import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import jp.nasta.pms.common.biz.enums.BssInventoryHistStatusEnum;
import jp.nasta.pms.common.biz.enums.BssInventoryLastStatusEnum;
import jp.nasta.pms.common.biz.enums.BssInventoryStatusEnum;
import jp.nasta.pms.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.nasta.pms.common.biz.utils.EntityAuditHelper;
import jp.nasta.pms.common.core.enums.DelFlgEnum;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssInventoryInboundLineBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssInventorySearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.*;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssInventoryMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IBssInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在庫情報 Service業務処理
 *
 * @author nextosd
 */
@Service
public class BssInventoryServiceImpl implements IBssInventoryService {

    /**
     * 登録プログラムＩＤ：パレット情報詳細
     */
    private static final String PG_ID = ExternalWarehousePgIdEnum.SP305.getCode();

    /**
     * 初期バージョン
     */
    private static final int INITIAL_VERSION = 1;

    /**
     * 登録者の最大桁数
     */
    private static final int OPERATOR_MAX = 20;

    /**
     * 入出庫担当者の最大桁数
     */
    private static final int IO_OPERATOR_MAX = 50;

    /**
     * 品目名の最大桁数
     */
    private static final int ITEM_NAME_MAX = 52;

    /**
     * 単位の最大桁数
     */
    private static final int UOM_MAX = 10;

    /**
     * バーコードの最大桁数
     */
    private static final int BARCODE_MAX = 20;

    /**
     * 入出庫日時の最大桁数
     */
    private static final int IO_DATETIME_MAX = 20;

    /**
     * 備考の最大桁数
     */
    private static final int REMARKS_MAX = 500;

    /**
     * 保管期間の最大桁数
     */
    private static final int STORAGE_PERIOD_MAX = 10;

    @Autowired
    private BssInventoryMapper bssInventoryMapper;

    /**
     * パレット番号表の初回印刷に合わせて、在庫情報詳細と在庫履歴を登録する
     *
     * @param pallet パレット情報（積載明細を含む）
     */
    @Override
    public void registerInboundFromPallet(BssPalletNumberVo pallet) {
        if (pallet == null || CollectionUtils.isEmpty(pallet.getDetails())) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.loadedItem")));
        }

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();
        String operator = StringUtils.substring(username, 0, OPERATOR_MAX);
        String ioOperator = StringUtils.substring(username, 0, IO_OPERATOR_MAX);
        String zoneCd = resolveZoneCd(pallet);
        String ioDatetime = resolveIoDatetime(pallet.getInboundDate(), now);
        String storagePeriod = StringUtils.substring(StringUtils.trimToNull(pallet.getStoragePeriod()), 0, STORAGE_PERIOD_MAX);
        Map<String, MastItemEntity> itemMap = loadItems(pallet.getDetails());

        for (BssPalletNumberDetailEntity line : pallet.getDetails()) {
            BssInventoryInboundLineBo inboundLine = toInboundLine(pallet, line, itemMap);
            insertDetail(pallet, inboundLine, now, operator);
            insertHistory(pallet, inboundLine, zoneCd, ioDatetime, ioOperator, storagePeriod, now, operator);
        }
    }

    /**
     * 画面の積載明細を、在庫登録で共通に使う内容へ揃える
     *
     * @param pallet  パレット情報
     * @param line    積載明細
     * @param itemMap 品目マスタ
     * @return 在庫登録行
     */
    private BssInventoryInboundLineBo toInboundLine(BssPalletNumberVo pallet, BssPalletNumberDetailEntity line,
                                      Map<String, MastItemEntity> itemMap) {
        if (StringUtils.isEmpty(line.getItemCode())) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.partItemCode")));
        }
        if (line.getStorageQty() == null) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.storageQty")));
        }
        if (line.getStorageQty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("E0069", MessageUtils.message("label.storageQty")));
        }

        MastItemEntity item = itemMap.get(line.getItemCode());
        String masterName = item == null ? null : item.getItemName();
        String masterUom = item == null ? null : item.getPrztic();
        String itemName = firstNonBlank(line.getItemName(), masterName, line.getItemCode());

        BssInventoryInboundLineBo inboundLine = new BssInventoryInboundLineBo();
        inboundLine.setItemCode(line.getItemCode().trim());
        inboundLine.setItemName(StringUtils.substring(itemName, 0, ITEM_NAME_MAX));
        inboundLine.setUom(StringUtils.substring(StringUtils.trimToNull(masterUom), 0, UOM_MAX));
        inboundLine.setQty(line.getStorageQty());
        inboundLine.setStorageType(StringUtils.trimToNull(line.getStorageType()));
        inboundLine.setRemarks(StringUtils.substring(firstNonBlank(line.getRemarks(), pallet.getMemo1()), 0, REMARKS_MAX));
        return inboundLine;
    }

    /**
     * 在庫情報詳細を登録する
     */
    private void insertDetail(BssPalletNumberVo pallet, BssInventoryInboundLineBo line, Date now, String operator) {
        BssInventoryDetailEntity detail = new BssInventoryDetailEntity();
        detail.setId(IdUtils.nextIdStr());
        detail.setPalletNo(pallet.getPalletNo());
        detail.setBarcode(resolveBarcode(pallet));
        detail.setWhCd(pallet.getWhCd());
        detail.setLocCd(pallet.getLocCd());
        detail.setClassification(pallet.getClassification());
        detail.setItemCode(line.getItemCode());
        detail.setItemName(line.getItemName());
        detail.setQty(line.getQty());
        detail.setStatus(BssInventoryStatusEnum.PA.getCode());
        detail.setLastStatus(BssInventoryLastStatusEnum.PO_INED.getCode());
        detail.setRemarks(line.getRemarks());
        detail.setDeleteFlg(DelFlgEnum.OFF.getCode());
        detail.setVersion(INITIAL_VERSION);
        EntityAuditHelper.fillForInsert(detail, now, operator, PG_ID);
        bssInventoryMapper.insertDetail(detail);
    }

    /**
     * 在庫履歴を登録する
     */
    private void insertHistory(BssPalletNumberVo pallet, BssInventoryInboundLineBo line, String zoneCd, String ioDatetime,
                               String ioOperator, String storagePeriod, Date now, String operator) {
        BssInventoryHistoryEntity history = new BssInventoryHistoryEntity();
        history.setId(IdUtils.nextIdStr());
        history.setWhCd(pallet.getWhCd());
        history.setZoneCd(zoneCd);
        history.setLocCd(pallet.getLocCd());
        history.setItemCode(line.getItemCode());
        history.setItemName(line.getItemName());
        history.setUom(line.getUom());
        history.setPalletNo(pallet.getPalletNo());
        history.setStatus(BssInventoryHistStatusEnum.IN.getCode());
        history.setStorageType(line.getStorageType());
        history.setStorageQty(line.getQty());
        history.setIoDatetime(ioDatetime);
        history.setIoOperator(ioOperator);
        history.setStoragePeriod(storagePeriod);
        history.setVersion(INITIAL_VERSION);
        EntityAuditHelper.fillForInsert(history, now, operator, PG_ID);
        bssInventoryMapper.insertHistory(history);
    }

    /**
     * 品目マスタを品目コードで引けるようにする
     */
    private Map<String, MastItemEntity> loadItems(List<BssPalletNumberDetailEntity> details) {
        List<String> itemCodes = details.stream()
                .map(BssPalletNumberDetailEntity::getItemCode)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(itemCodes)) {
            return new HashMap<>();
        }
        Map<String, MastItemEntity> itemMap = new HashMap<>();
        for (MastItemEntity item : bssInventoryMapper.selectItemsByCodes(itemCodes)) {
            if (item != null && StringUtils.isNotEmpty(item.getItemCode())) {
                itemMap.putIfAbsent(item.getItemCode(), item);
            }
        }
        return itemMap;
    }

    /**
     * 保管エリアはパレットの値を優先し、未設定ならロケーションマスタから補う
     */
    private String resolveZoneCd(BssPalletNumberVo pallet) {
        String zoneCd = StringUtils.trimToNull(pallet.getZoneCd());
        if (zoneCd == null && StringUtils.isNotEmpty(pallet.getWhCd()) && StringUtils.isNotEmpty(pallet.getLocCd())) {
            zoneCd = StringUtils.trimToNull(bssInventoryMapper.selectZoneCd(pallet.getWhCd(), pallet.getLocCd()));
        }
        // 在庫履歴の保管エリアは必須のため、未設定は空文字で登録する
        return zoneCd == null ? StringUtils.EMPTY : zoneCd;
    }

    /**
     * 入出庫日時は画面の入庫日を使い、未入力のときは印刷時点とする
     */
    private String resolveIoDatetime(String inboundDate, Date now) {
        String value = StringUtils.trimToNull(inboundDate);
        if (value != null && value.length() <= IO_DATETIME_MAX) {
            return value;
        }
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, now);
    }

    /**
     * 在庫一覧を取得する
     */
    @Override
    public List<BssInventoryListVo> selectStockList(BssInventorySearchBo searchBo) {
        return bssInventoryMapper.selectStockList(searchBo == null ? new BssInventorySearchBo() : searchBo);
    }

    /**
     * 品目の在庫詳細を取得する
     */
    @Override
    public BssInventoryItemVo selectItemDetail(String itemCode) {
        String code = StringUtils.trimToNull(itemCode);
        if (code == null) {
            return null;
        }
        BssInventoryItemVo header = bssInventoryMapper.selectItemHeader(code);
        if (header == null) {
            return null;
        }
        List<BssInventoryPalletVo> pallets = bssInventoryMapper.selectPalletsByItemCode(code);
        header.setPallets(pallets == null ? List.of() : pallets);
        header.setHistories(groupHistories(bssInventoryMapper.selectHistoryByItemCode(code)));
        return header;
    }

    /**
     * ロケ番の在庫詳細を取得する
     */
    @Override
    public BssInventoryLocationVo selectLocationDetail(String whCd, String locCd) {
        String warehouseCode = StringUtils.trimToNull(whCd);
        String locationCode = StringUtils.trimToNull(locCd);
        if (warehouseCode == null || locationCode == null) {
            return null;
        }
        BssInventoryLocationVo header = bssInventoryMapper.selectLocationHeader(warehouseCode, locationCode);
        if (header == null) {
            return null;
        }
        header.setPallets(groupLocationPallets(bssInventoryMapper.selectLocationLines(warehouseCode, locationCode)));
        return header;
    }

    /**
     * 移動履歴をパレット番号ごとにまとめる
     */
    private List<BssInventoryHistoryGroupVo> groupHistories(List<BssInventoryHistoryFlatVo> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return List.of();
        }
        Map<String, BssInventoryHistoryGroupVo> groups = new LinkedHashMap<>();
        for (BssInventoryHistoryFlatVo row : rows) {
            String key = StringUtils.defaultString(row.getPalletNo());
            BssInventoryHistoryGroupVo group = groups.computeIfAbsent(key, ignored -> {
                BssInventoryHistoryGroupVo created = new BssInventoryHistoryGroupVo();
                created.setPalletNo(row.getPalletNo());
                created.setStoragePeriod(row.getStoragePeriod());
                created.setPlace(row.getPlace());
                created.setRows(new ArrayList<>());
                return created;
            });
            BssInventoryHistoryRowVo line = new BssInventoryHistoryRowVo();
            line.setId(row.getId());
            line.setStatus(row.getStatus());
            line.setWarehouse(row.getWarehouse());
            line.setQuantity(row.getQuantity());
            line.setUsedQty(row.getUsedQty());
            line.setLostQty(row.getLostQty());
            line.setMoveAt(row.getMoveAt());
            line.setRequestNo(row.getRequestNo());
            line.setRequestAt(row.getRequestAt());
            line.setRequester(row.getRequester());
            line.setUpdatedAt(row.getUpdatedAt());
            line.setUpdater(row.getUpdater());
            group.getRows().add(line);
        }
        return new ArrayList<>(groups.values());
    }

    /**
     * ロケ番の在庫をパレット番号ごとにまとめる
     */
    private List<BssInventoryLocationPalletVo> groupLocationPallets(List<BssInventoryLocationFlatVo> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return List.of();
        }
        Map<String, BssInventoryLocationPalletVo> groups = new LinkedHashMap<>();
        for (BssInventoryLocationFlatVo row : rows) {
            String key = StringUtils.defaultString(row.getPalletNo());
            BssInventoryLocationPalletVo group = groups.computeIfAbsent(key, ignored -> {
                BssInventoryLocationPalletVo created = new BssInventoryLocationPalletVo();
                created.setPalletNo(row.getPalletNo());
                created.setStoragePeriod(row.getStoragePeriod());
                created.setItems(new ArrayList<>());
                return created;
            });
            BssInventoryLocationItemVo item = new BssInventoryLocationItemVo();
            item.setId(row.getId());
            item.setItemCode(row.getItemCode());
            item.setItemName(row.getItemName());
            item.setStorageType(row.getStorageType());
            item.setQuantity(row.getQuantity());
            item.setUsable(row.getUsable());
            group.getItems().add(item);
        }
        return new ArrayList<>(groups.values());
    }

    /**
     * バーコードはパレット番号表の値を使い、未設定ならパレット番号とする
     */
    private String resolveBarcode(BssPalletNumberVo pallet) {
        String barcode = firstNonBlank(pallet.getBarcode(), pallet.getPalletNo());
        return StringUtils.substring(barcode, 0, BARCODE_MAX);
    }

    /**
     * 先に値が入っている文字列を返す
     */
    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            String trimmed = StringUtils.trimToNull(value);
            if (trimmed != null) {
                return trimmed;
            }
        }
        return null;
    }
}
