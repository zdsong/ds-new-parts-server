package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.domain.entity.BssInventoryDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssInventoryHistoryEntity;
import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssTransferDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssTransferEntity;
import jp.nasta.pms.common.biz.domain.entity.MastItemEntity;
import jp.nasta.pms.common.biz.enums.*;
import jp.nasta.pms.common.biz.service.IExternalWarehouseCommonService;
import jp.nasta.pms.common.biz.utils.EntityAuditHelper;
import jp.nasta.pms.common.core.service.ISysRangedSeqService;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveCompleteBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveRegisterBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveRegisterLineBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssMoveStockSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssPalletNumberBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderDetailBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveDetailLineVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveListVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveRegisterVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssMoveStockVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferCandidateVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssInventoryMapper;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssTransferMapper;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssTransferOrderMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IBssPalletNumberService;
import jp.nasta.pms.externalwarehouse.biz.service.IBssTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 移動依頼 Service業務処理
 */
@Service
public class BssTransferOrderServiceImpl implements IBssTransferOrderService {

    /**
     * 移動依頼番号の接頭辞（出庫）
     */
    private static final String REQUEST_NO_PREFIX = "O";

    /**
     * 移動依頼番号の採番キー
     */
    private static final String SEQ_KEY_TRANSFER_REQUEST = "transfer_request";

    /**
     * 移動番号の接頭辞
     */
    private static final String MOVE_NO_PREFIX = "M";

    /**
     * 移動番号の採番キー
     */
    private static final String SEQ_KEY_TRANSFER_MOVE = "transfer_move";

    /**
     * 登録プログラムＩＤ：移動依頼（出庫）
     */
    private static final String PG_ID_CREATE = ExternalWarehousePgIdEnum.P401.getCode();

    /**
     * 登録プログラムＩＤ：移動登録（SP402）
     */
    private static final String PG_ID_REGISTER = ExternalWarehousePgIdEnum.P402.getCode();

    /**
     * 登録プログラムＩＤ：パレット受取（SP406）
     */
    private static final String PG_ID_RECEIVE = ExternalWarehousePgIdEnum.SP406.getCode();

    @Autowired
    private BssTransferOrderMapper bssTransferOrderMapper;

    @Autowired
    private BssTransferMapper bssTransferMapper;

    @Autowired
    private BssInventoryMapper bssInventoryMapper;

    @Autowired
    private IExternalWarehouseCommonService commonService;

    @Autowired
    private IBssPalletNumberService bssPalletNumberService;

    @Autowired
    private ISysRangedSeqService sysRangedSeqService;

    /**
     * 移動依頼一覧を取得する
     */
    @Override
    public List<BssTransferListVo> selectTransferList(BssTransferSearchBo searchBo) {
        BssTransferSearchBo condition = searchBo == null ? new BssTransferSearchBo() : searchBo;
        condition.setStatuses(splitStatusCodes(condition.getStatusCodes()));
        List<BssTransferListVo> rows = bssTransferOrderMapper.selectTransferList(condition);
        if (CollectionUtils.isEmpty(rows)) {
            return List.of();
        }
        List<String> requestNos = rows.stream().map(BssTransferListVo::getRequestNo).toList();
        Map<String, List<BssTransferItemVo>> itemsByRequestNo = new LinkedHashMap<>();
        for (BssTransferItemVo item : bssTransferOrderMapper.selectItemsByRequestNos(requestNos)) {
            itemsByRequestNo.computeIfAbsent(item.getRequestNo(), ignored -> new ArrayList<>()).add(item);
        }
        for (BssTransferListVo row : rows) {
            row.setItems(itemsByRequestNo.getOrDefault(row.getRequestNo(), List.of()));
        }
        return rows;
    }

    /**
     * 移動依頼（出庫）の対象となる在庫明細を取得する
     */
    @Override
    public List<BssTransferCandidateVo> selectTransferCandidates(BssTransferCandidateSearchBo searchBo) {
        BssTransferCandidateSearchBo condition =
                searchBo == null ? new BssTransferCandidateSearchBo() : searchBo;
        // 移動依頼の対象は在庫状況が在庫の明細のみ
        condition.setStatus(BssInventoryStatusEnum.PA.getCode());
        condition.setClassifications(splitStatusCodes(condition.getClassificationCodes()));
        return bssTransferOrderMapper.selectTransferCandidates(condition);
    }

    /**
     * 移動依頼を登録する
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTransferOrder(BssTransferOrderBo bo) {
        if (bo == null || CollectionUtils.isEmpty(bo.getDetails())) {
            throw new ServiceException(MessageUtils.message("E0070"));
        }
        if (StringUtils.isEmpty(bo.getDestWhCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.warehouse")));
        }

        String username = SecurityUtils.getLoginUser().getUsername();
        String today = DateUtils.dateTime(new java.util.Date());
        String requestNo = generateRequestNo();

        bo.setId(IdUtils.nextIdStr());
        bo.setRequestNo(requestNo);
        // 登録直後の移動状況は移動依頼
        bo.setStatus(BssTransferStatusEnum.TF.getCode());
        bo.setTransferDate(today);
        bo.setRegistrationDate(today);
        bo.setRegisteredBy(username);
        if (StringUtils.isEmpty(bo.getRequester())) {
            bo.setRequester(username);
        }
        bo.setCreatePgId(PG_ID_CREATE);
        bssTransferOrderMapper.insertTransferOrder(bo);

        List<BssTransferOrderDetailBo> details = bo.getDetails();
        for (int index = 0; index < details.size(); index++) {
            BssTransferOrderDetailBo detail = details.get(index);
            detail.setId(IdUtils.nextIdStr());
            detail.setTransferRequestNo(requestNo);
            detail.setDetailNo(index + 1);
            detail.setStatus(BssTransferStatusEnum.TF.getCode());
            detail.setCreateBy(username);
            detail.setCreatePgId(PG_ID_CREATE);
        }
        bssTransferOrderMapper.insertTransferOrderDetails(details);
        return requestNo;
    }

    /**
     * 移動依頼詳細を取得する
     */
    @Override
    public BssTransferDetailVo selectTransferDetail(String requestNo) {
        if (StringUtils.isEmpty(requestNo)) {
            return null;
        }
        BssTransferDetailVo detail = bssTransferOrderMapper.selectTransferDetailByRequestNo(requestNo);
        if (detail == null) {
            return null;
        }
        detail.setDetails(bssTransferOrderMapper.selectTransferDetailLines(requestNo));
        return detail;
    }

    /**
     * 移動依頼を更新する（ヘッダ・明細の移動数）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransferOrder(BssTransferOrderBo bo) {
        if (bo == null || StringUtils.isEmpty(bo.getRequestNo())) {
            throw new ServiceException(MessageUtils.message("E0029",
                    MessageUtils.message("label.transferRequestNo")));
        }
        if (StringUtils.isEmpty(bo.getDestWhCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.warehouse")));
        }
        if (CollectionUtils.isEmpty(bo.getDetails())) {
            throw new ServiceException(MessageUtils.message("E0070"));
        }

        BssTransferDetailVo current = bssTransferOrderMapper
                .selectTransferDetailByRequestNo(bo.getRequestNo());
        if (current == null) {
            throw new ServiceException(MessageUtils.message("E0016",
                    MessageUtils.message("label.transferRequest")));
        }

        String username = SecurityUtils.getLoginUser().getUsername();
        bo.setUpdateBy(username);
        bo.setUpdatePgId(PG_ID_CREATE);
        bssTransferOrderMapper.updateTransferOrder(bo);

        int maxDetailNo = bssTransferOrderMapper.selectMaxDetailNo(bo.getRequestNo());
        List<BssTransferOrderDetailBo> inserts = new ArrayList<>();
        List<Integer> keepDetailNos = new ArrayList<>();
        for (BssTransferOrderDetailBo detail : bo.getDetails()) {
            if (detail.getDetailNo() == null) {
                // 画面で追加された明細は新しい明細番号で登録する
                detail.setId(IdUtils.nextIdStr());
                detail.setTransferRequestNo(bo.getRequestNo());
                detail.setDetailNo(++maxDetailNo);
                detail.setStatus(BssTransferStatusEnum.TF.getCode());
                detail.setCreateBy(username);
                detail.setCreatePgId(PG_ID_CREATE);
                inserts.add(detail);
            } else {
                // 既存明細は移動数を更新する
                detail.setTransferRequestNo(bo.getRequestNo());
                detail.setUpdateBy(username);
                detail.setUpdatePgId(PG_ID_CREATE);
                bssTransferOrderMapper.updateTransferOrderDetail(detail);
            }
            keepDetailNos.add(detail.getDetailNo());
        }
        if (!inserts.isEmpty()) {
            bssTransferOrderMapper.insertTransferOrderDetails(inserts);
        }
        // 画面から削除された明細は論理削除する
        bssTransferOrderMapper.deleteTransferOrderDetails(
                bo.getRequestNo(), keepDetailNos, username, PG_ID_CREATE);
    }

    /**
     * 移動一覧を取得する
     */
    @Override
    public List<BssMoveListVo> selectMoveList(BssMoveSearchBo searchBo) {
        BssMoveSearchBo condition = searchBo == null ? new BssMoveSearchBo() : searchBo;
        condition.setStatuses(splitStatusCodes(condition.getStatusCodes()));
        List<BssMoveListVo> rows = bssTransferMapper.selectMoveList(condition);
        if (CollectionUtils.isEmpty(rows)) {
            return List.of();
        }
        List<String> transferNos = rows.stream().map(BssMoveListVo::getTransferNo).toList();
        Map<String, List<BssMoveItemVo>> itemsByTransferNo = new LinkedHashMap<>();
        for (BssMoveItemVo item : bssTransferMapper.selectMoveItemsByTransferNos(transferNos)) {
            itemsByTransferNo.computeIfAbsent(item.getTransferNo(), ignored -> new ArrayList<>()).add(item);
        }
        for (BssMoveListVo row : rows) {
            row.setItems(itemsByTransferNo.getOrDefault(row.getTransferNo(), List.of()));
        }
        return rows;
    }

    /**
     * 移動情報詳細を取得する
     */
    @Override
    public BssMoveDetailVo selectMoveDetail(String transferNo) {
        if (StringUtils.isEmpty(transferNo)) {
            return null;
        }
        BssMoveDetailVo detail = bssTransferMapper.selectMoveDetail(transferNo);
        if (detail == null) {
            return null;
        }
        detail.setDetails(bssTransferMapper.selectMoveDetailLines(transferNo));
        return detail;
    }

    /**
     * 移動元の在庫明細を取得する
     */
    @Override
    public List<BssMoveStockVo> selectMoveStockList(BssMoveStockSearchBo searchBo) {
        BssMoveStockSearchBo condition = searchBo == null ? new BssMoveStockSearchBo() : searchBo;
        // 移動対象は在庫状況が在庫の明細のみ
        condition.setStatus(BssInventoryStatusEnum.PA.getCode());
        return bssTransferMapper.selectMoveStockList(condition);
    }

    /**
     * パレット番号から移動登録済みの移動情報を取得する（SP406 読取）
     */
    @Override
    public BssMoveDetailVo selectReceiveMove(String palletNo) {
        if (StringUtils.isEmpty(palletNo)) {
            return null;
        }
        String transferNo = bssTransferMapper.selectPendingTransferNoByPalletNo(
                palletNo.trim(), BssTransferStatusEnum.TF_P.getCode());
        if (StringUtils.isEmpty(transferNo)) {
            return null;
        }
        return selectMoveDetail(transferNo);
    }

    /**
     * 移動完了を行う（SP406）。
     * 移動先パレットの在庫が移動明細と一致する場合は倉庫・ロケ番だけを更新し、
     * 一致しない場合は移動先の在庫を新規登録して移動元の数量・引当数量を減らす。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeMove(BssMoveCompleteBo bo) {
        if (bo == null || StringUtils.isEmpty(bo.getTransferNo())) {
            throw new ServiceException(MessageUtils.message("E0029",
                    MessageUtils.message("label.transferNo")));
        }
        if (StringUtils.isEmpty(bo.getDestinationWhCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.warehouse")));
        }
        if (StringUtils.isEmpty(bo.getDestinationLocCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.locationCd")));
        }

        BssMoveDetailVo move = selectMoveDetail(bo.getTransferNo());
        if (move == null || !BssTransferStatusEnum.TF_P.getCode().equals(move.getStatus())) {
            throw new ServiceException(MessageUtils.message("E0016",
                    MessageUtils.message("label.transfer")));
        }
        List<BssMoveDetailLineVo> lines = bssTransferMapper.selectMoveDetailLines(bo.getTransferNo());
        if (CollectionUtils.isEmpty(lines)) {
            throw new ServiceException(MessageUtils.message("E0070"));
        }

        // 移動先パレットは明細で共通のはず
        Set<String> destPalletNos = lines.stream()
                .map(BssMoveDetailLineVo::getDestPalletNo)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
        if (destPalletNos.size() != 1) {
            throw new ServiceException(MessageUtils.message("E0016",
                    MessageUtils.message("label.palletNo")));
        }
        String destPalletNo = destPalletNos.iterator().next();

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();
        String completedAt = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, now);

        // 移動先パレットの在庫が明細と完全に一致するか判定する
        List<BssInventoryDetailEntity> destStock = bssInventoryMapper
                .selectPaDetailsByPalletNo(destPalletNo);
        if (isSamePalletInventory(lines, destStock)) {
            // 同じパレット・同じ品目・同じ数量のため、倉庫とロケ番だけを移動先へ変更する
            for (BssInventoryDetailEntity stock : destStock) {
                bssInventoryMapper.updateReceivedInventory(stock.getId(),
                        bo.getDestinationWhCd(), bo.getDestinationLocCd(),
                        BssInventoryLastStatusEnum.TF_INED.getCode(), username, PG_ID_RECEIVE);
            }
        } else {
            // パレット番号が異なるため、移動先の在庫を新規登録し、移動元の数量を減らす
            for (BssMoveDetailLineVo line : lines) {
                if (StringUtils.isNotEmpty(line.getInventoryDetailId())) {
                    bssInventoryMapper.decreaseInventoryForReceived(line.getInventoryDetailId(),
                            line.getMoveQty(), username, PG_ID_RECEIVE);
                    // 移動元の数量が 0 になった明細は物理削除する
                    bssInventoryMapper.deleteInventoryIfEmpty(line.getInventoryDetailId());
                }
                BssInventoryDetailEntity received = new BssInventoryDetailEntity();
                received.setId(IdUtils.nextIdStr());
                received.setPalletNo(destPalletNo);
                received.setBarcode(destPalletNo);
                received.setWhCd(bo.getDestinationWhCd());
                received.setLocCd(bo.getDestinationLocCd());
                received.setClassification(line.getClassificationCode());
                received.setItemCode(line.getItemCode());
                received.setItemName(line.getItemName());
                received.setQty(line.getMoveQty());
                received.setAllocationQty(BigDecimal.ZERO);
                received.setStatus(BssInventoryStatusEnum.PA.getCode());
                received.setLastStatus(BssInventoryLastStatusEnum.TF_INED.getCode());
                received.setDeleteFlg("0");
                received.setVersion(1);
                EntityAuditHelper.fillForInsert(received, now, username, PG_ID_RECEIVE);
                bssInventoryMapper.insertDetail(received);
            }
        }

        int rows = bssTransferMapper.updateTransferCompleted(bo.getTransferNo(),
                BssTransferStatusEnum.TF_F.getCode(), BssTransferStatusEnum.TF_P.getCode(),
                StringUtils.trimToNull(bo.getCompletionMemo()), username, completedAt,
                username, PG_ID_RECEIVE);
        if (rows == 0) {
            throw new ServiceException(MessageUtils.message("E0016",
                    MessageUtils.message("label.transfer")));
        }
        bssTransferMapper.updateTransferDetailStatus(bo.getTransferNo(),
                BssTransferStatusEnum.TF_F.getCode(), username, PG_ID_RECEIVE);
    }

    /**
     * 移動先パレットの在庫が移動明細と同じ品目・同じ数量かを判定する
     */
    private boolean isSamePalletInventory(List<BssMoveDetailLineVo> lines,
                                          List<BssInventoryDetailEntity> stock) {
        if (CollectionUtils.isEmpty(stock) || stock.size() != lines.size()) {
            return false;
        }
        Map<String, BigDecimal> moveQtyByItem = new HashMap<>();
        for (BssMoveDetailLineVo line : lines) {
            moveQtyByItem.merge(line.getItemCode(),
                    line.getMoveQty() == null ? BigDecimal.ZERO : line.getMoveQty(), BigDecimal::add);
        }
        Map<String, BigDecimal> stockQtyByItem = new HashMap<>();
        for (BssInventoryDetailEntity row : stock) {
            stockQtyByItem.merge(row.getItemCode(),
                    row.getQty() == null ? BigDecimal.ZERO : row.getQty(), BigDecimal::add);
        }
        if (!moveQtyByItem.keySet().equals(stockQtyByItem.keySet())) {
            return false;
        }
        for (Map.Entry<String, BigDecimal> entry : moveQtyByItem.entrySet()) {
            if (entry.getValue().compareTo(stockQtyByItem.get(entry.getKey())) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 移動登録を行う。
     * 移動管理（bss_transfer／bss_transfer_detail）を移動登録済（TF_P）で登録し、
     * 在庫の割り当て数量・在庫履歴・移動依頼明細の移動済数量を更新する。
     * 在庫のバージョンが画面の読み取り時と異なる場合は全体をロールバックする。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BssMoveRegisterVo registerMove(BssMoveRegisterBo bo) {
        if (bo == null || CollectionUtils.isEmpty(bo.getLines())) {
            throw new ServiceException(MessageUtils.message("E0070"));
        }
        if (StringUtils.isEmpty(bo.getDestinationWhCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.warehouse")));
        }
        if (StringUtils.isEmpty(bo.getDestinationLocCd())) {
            throw new ServiceException(MessageUtils.message("E0023",
                    MessageUtils.message("label.locationCd")));
        }
        if (StringUtils.isEmpty(StringUtils.trim(bo.getRegistrar()))) {
            throw new ServiceException(MessageUtils.message("E0003",
                    MessageUtils.message("label.registrar")));
        }

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();
        String registeredAt = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, now);

        // 在庫を読み、移動数量が保管数量と残数量の範囲内かを確認する
        Map<String, BssInventoryDetailEntity> detailById = new LinkedHashMap<>();
        Map<String, BigDecimal> moveQtyById = new LinkedHashMap<>();
        Set<String> usedRequestDetails = new HashSet<>();
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String detailId = StringUtils.trimToNull(line.getId());
            if (detailId == null) {
                throw new ServiceException(MessageUtils.message("E0016",
                        MessageUtils.message("label.inventoryInfo")));
            }
            if (detailById.containsKey(detailId)) {
                throw new ServiceException(MessageUtils.message("E0012",
                        MessageUtils.message("label.itemLine")));
            }
            BssInventoryDetailEntity detail = bssInventoryMapper.selectDetailById(detailId);
            if (detail == null || !BssInventoryStatusEnum.PA.getCode().equals(detail.getStatus())) {
                throw new ServiceException(MessageUtils.message("E0016",
                        MessageUtils.message("label.inventoryInfo")));
            }
            BigDecimal moveQty = line.getMoveQty();
            if (moveQty == null || moveQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException(MessageUtils.message("E0069",
                        MessageUtils.message("label.moveQty")));
            }
            BigDecimal stockQty = detail.getQty() == null ? BigDecimal.ZERO : detail.getQty();
            if (moveQty.compareTo(stockQty) > 0) {
                throw new ServiceException(MessageUtils.message("E0071"));
            }
            BigDecimal allocationQty = detail.getAllocationQty() == null
                    ? BigDecimal.ZERO : detail.getAllocationQty();
            if (moveQty.compareTo(stockQty.subtract(allocationQty)) > 0) {
                throw new ServiceException(MessageUtils.message("E0072"));
            }

            // 移動依頼がある明細は、依頼の残数量を超えないことを確認する
            String requestNo = StringUtils.trimToNull(line.getRequestNo());
            if (requestNo != null) {
                if (line.getRequestDetailNo() == null) {
                    throw new ServiceException(MessageUtils.message("E0029",
                            MessageUtils.message("label.transferRequestNo")));
                }
                String requestKey = requestNo + ":" + line.getRequestDetailNo();
                if (usedRequestDetails.contains(requestKey)) {
                    throw new ServiceException(MessageUtils.message("E0012",
                            MessageUtils.message("label.itemLine")));
                }
                BssTransferOrderDetailBo requestDetail = bssTransferMapper
                        .selectRequestDetailForMove(requestNo, line.getRequestDetailNo());
                if (requestDetail == null) {
                    throw new ServiceException(MessageUtils.message("E0016",
                            MessageUtils.message("label.transferRequest")));
                }
                BigDecimal requestQty = requestDetail.getUsedQty() == null
                        ? BigDecimal.ZERO : requestDetail.getUsedQty();
                BigDecimal movedQty = requestDetail.getMovedQty() == null
                        ? BigDecimal.ZERO : requestDetail.getMovedQty();
                if (moveQty.compareTo(requestQty.subtract(movedQty)) > 0) {
                    throw new ServiceException(MessageUtils.message("E0072"));
                }
                usedRequestDetails.add(requestKey);
            }

            detailById.put(detailId, detail);
            moveQtyById.put(detailId, moveQty);
        }

        // 依存がなく、パレットの全品目を全量移動する場合はパレット番号を発番しない
        boolean wholePallet = !bo.isDependent() && isWholePalletMove(bo, detailById, moveQtyById);
        String palletNo;
        String classification;
        if (wholePallet) {
            palletNo = detailById.values().iterator().next().getPalletNo();
            classification = resolveClassification(bo, detailById);
        } else {
            classification = resolveClassification(bo, detailById);
            palletNo = commonService.generatePalletNo(classification);
            // 発番したパレットはパレット情報（ヘッダ・積載明細）にも登録する
            insertMovePallet(palletNo, classification, bo, detailById, moveQtyById);
        }

        // 移動管理（ヘッダ）を登録する
        String transferNo = generateTransferNo();
        BssTransferEntity transfer = new BssTransferEntity();
        transfer.setId(IdUtils.nextIdStr());
        transfer.setTransferNo(transferNo);
        transfer.setStatus(BssTransferStatusEnum.TF_P.getCode());
        transfer.setTransferFmWhCd(detailById.values().iterator().next().getWhCd());
        transfer.setTransferToWhCd(bo.getDestinationWhCd());
        transfer.setTransferToLocCd(bo.getDestinationLocCd());
        transfer.setTransferRegisteredBy(bo.getRegistrar().trim());
        transfer.setTransferRegisteredAt(registeredAt);
        transfer.setMemo(StringUtils.trimToNull(bo.getRemark()));
        transfer.setDeleteFlg("0");
        transfer.setVersion(1);
        EntityAuditHelper.fillForInsert(transfer, now, username, PG_ID_REGISTER);
        bssTransferMapper.insertTransfer(transfer);

        // 移動管理（明細）を登録する
        List<BssTransferDetailEntity> transferDetails = new ArrayList<>();
        int detailNo = 0;
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String detailId = line.getId().trim();
            BssInventoryDetailEntity detail = detailById.get(detailId);
            BssTransferDetailEntity transferDetail = new BssTransferDetailEntity();
            transferDetail.setId(IdUtils.nextIdStr());
            transferDetail.setTransferNo(transferNo);
            transferDetail.setDetailNo(++detailNo);
            transferDetail.setTransferRequestNo(StringUtils.trimToNull(line.getRequestNo()));
            transferDetail.setTransferRequestDetailNo(line.getRequestDetailNo());
            transferDetail.setInventoryDetailId(detailId);
            transferDetail.setItemCode(detail.getItemCode());
            transferDetail.setItemName(detail.getItemName());
            transferDetail.setClassification(classification);
            transferDetail.setStorageQty(detail.getQty());
            transferDetail.setMoveQty(moveQtyById.get(detailId));
            transferDetail.setLostDamagedQty(BigDecimal.ZERO);
            transferDetail.setDisposalQty(BigDecimal.ZERO);
            transferDetail.setSourcePalletNo(detail.getPalletNo());
            transferDetail.setDestPalletNo(palletNo);
            transferDetail.setPalletIssueFlg(wholePallet ? "0" : "1");
            transferDetail.setStatus(BssTransferStatusEnum.TF_P.getCode());
            transferDetail.setDeleteFlg("0");
            transferDetail.setVersion(1);
            EntityAuditHelper.fillForInsert(transferDetail, now, username, PG_ID_REGISTER);
            transferDetails.add(transferDetail);
        }
        bssTransferMapper.insertTransferDetails(transferDetails);

        // 在庫の割り当て数量を更新し、在庫履歴を登録する。
        // バージョン不一致の場合は更新件数が 0 になり、トランザクション全体がロールバックされる
        Map<String, MastItemEntity> itemMap = loadItemMap(transferDetails.stream()
                .map(BssTransferDetailEntity::getItemCode)
                .toList());
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String detailId = line.getId().trim();
            BssInventoryDetailEntity detail = detailById.get(detailId);
            detail.setLastStatus(BssInventoryLastStatusEnum.TF_OUTED.getCode());
            int rows = bssInventoryMapper.updateAllocationQty(
                    detailId, moveQtyById.get(detailId), line.getVersion(), username, PG_ID_REGISTER,
                    BssInventoryLastStatusEnum.TF_OUTED.getCode());
            if (rows == 0) {
                throw new ServiceException(MessageUtils.message("E0007"));
            }
            insertMoveHistory(detail, itemMap.get(detail.getItemCode()), palletNo, transferNo,
                    StringUtils.trimToNull(line.getRequestNo()), moveQtyById.get(detailId),
                    bo.getRegistrar().trim(), registeredAt, now, username);
        }

        // 移動依頼の移動済数量を加算し、全明細が移動済みになったら依頼を移動登録完了にする
        Set<String> requestNos = new HashSet<>();
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String requestNo = StringUtils.trimToNull(line.getRequestNo());
            if (requestNo == null) {
                continue;
            }
            bssTransferMapper.addRequestDetailMovedQty(requestNo, line.getRequestDetailNo(),
                    moveQtyById.get(line.getId().trim()), BssTransferStatusEnum.TF_P.getCode(),
                    username, PG_ID_REGISTER);
            requestNos.add(requestNo);
        }
        for (String requestNo : requestNos) {
            bssTransferMapper.updateRequestStatusWhenCompleted(requestNo,
                    BssTransferStatusEnum.TF_P.getCode(), username, PG_ID_REGISTER);
        }

        BssMoveRegisterVo result = new BssMoveRegisterVo();
        result.setTransferNo(transferNo);
        result.setPalletNo(palletNo);
        result.setMode(wholePallet ? "whole" : "issue");
        return result;
    }

    /**
     * 発番したパレットをパレット情報（ヘッダ・積載明細）へ登録する。
     * 移動先の倉庫・ロケ番を保管場所とし、明細は移動数量で登録する。
     */
    private void insertMovePallet(String palletNo, String classification, BssMoveRegisterBo bo,
                                  Map<String, BssInventoryDetailEntity> detailById,
                                  Map<String, BigDecimal> moveQtyById) {
        BssPalletNumberBo pallet = new BssPalletNumberBo();
        pallet.setPalletNo(palletNo);
        pallet.setClassification(classification == null ? StringUtils.EMPTY : classification);
        pallet.setWhCd(bo.getDestinationWhCd());
        pallet.setLocCd(bo.getDestinationLocCd());
        pallet.setZoneCd(StringUtils.trimToNull(bssInventoryMapper.selectZoneCd(
                bo.getDestinationWhCd(), bo.getDestinationLocCd())));
        pallet.setStoragePeriod(StringUtils.EMPTY);
        pallet.setMemo1(StringUtils.EMPTY);
        pallet.setMemo2(StringUtils.EMPTY);

        List<BssPalletNumberDetailEntity> details = new ArrayList<>();
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String detailId = line.getId().trim();
            BssInventoryDetailEntity detail = detailById.get(detailId);
            BssPalletNumberDetailEntity palletDetail = new BssPalletNumberDetailEntity();
            palletDetail.setItemCode(detail.getItemCode());
            palletDetail.setItemName(detail.getItemName());
            palletDetail.setStorageQty(moveQtyById.get(detailId));
            details.add(palletDetail);
        }
        pallet.setDetails(details);
        bssPalletNumberService.insertPallet(pallet);
    }

    /**
     * パレットの全品目を、在庫数量と同じ数量で移動するか判定する
     */
    private boolean isWholePalletMove(BssMoveRegisterBo bo,
                                      Map<String, BssInventoryDetailEntity> detailById,
                                      Map<String, BigDecimal> moveQtyById) {
        Set<String> palletNos = new HashSet<>();
        for (BssInventoryDetailEntity detail : detailById.values()) {
            String palletNo = StringUtils.trimToNull(detail.getPalletNo());
            if (palletNo != null) {
                palletNos.add(palletNo);
            }
        }
        if (palletNos.size() != 1) {
            return false;
        }
        String palletNo = palletNos.iterator().next();
        if (StringUtils.isNotEmpty(bo.getPalletNo()) && !palletNo.equals(bo.getPalletNo().trim())) {
            return false;
        }
        List<BssInventoryDetailEntity> palletDetails = bssInventoryMapper.selectPaDetailsByPalletNo(palletNo);
        if (CollectionUtils.isEmpty(palletDetails) || palletDetails.size() != detailById.size()) {
            return false;
        }
        for (BssInventoryDetailEntity palletDetail : palletDetails) {
            BigDecimal moveQty = moveQtyById.get(palletDetail.getId());
            if (moveQty == null || palletDetail.getQty() == null
                    || moveQty.compareTo(palletDetail.getQty()) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 新しいパレット番号の品目区分を決める
     */
    private String resolveClassification(BssMoveRegisterBo bo,
                                         Map<String, BssInventoryDetailEntity> detailById) {
        for (BssMoveRegisterLineBo line : bo.getLines()) {
            String classification = StringUtils.trimToNull(line.getCategory());
            if (classification != null) {
                return classification;
            }
        }
        for (BssInventoryDetailEntity detail : detailById.values()) {
            String classification = StringUtils.trimToNull(detail.getClassification());
            if (classification != null) {
                return classification;
            }
        }
        return null;
    }

    /**
     * 品目コードから品目マスタを引けるようにする
     */
    private Map<String, MastItemEntity> loadItemMap(List<String> itemCodes) {
        List<String> codes = itemCodes.stream()
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());
        if (codes.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, MastItemEntity> itemMap = new HashMap<>();
        for (MastItemEntity item : bssInventoryMapper.selectItemsByCodes(codes)) {
            if (item != null && StringUtils.isNotEmpty(item.getItemCode())) {
                itemMap.putIfAbsent(item.getItemCode(), item);
            }
        }
        return itemMap;
    }

    /**
     * 移動予定の在庫履歴を登録する
     */
    private void insertMoveHistory(BssInventoryDetailEntity detail, MastItemEntity item, String palletNo,
                                   String transferNo, String requestNo, BigDecimal moveQty,
                                   String requester, String registeredAt, Date now, String username) {
        String zoneCd = StringUtils.trimToNull(bssInventoryMapper.selectZoneCd(
                detail.getWhCd(), detail.getLocCd()));
        BssInventoryHistoryEntity history = new BssInventoryHistoryEntity();
        history.setId(IdUtils.nextIdStr());
        history.setWhCd(detail.getWhCd());
        // 保管エリアは必須のため、未設定は空文字で登録する
        history.setZoneCd(zoneCd == null ? StringUtils.EMPTY : zoneCd);
        history.setLocCd(detail.getLocCd());
        history.setItemCode(detail.getItemCode());
        history.setItemName(detail.getItemName());
        history.setUom(item == null ? null : StringUtils.substring(
                StringUtils.trimToNull(item.getPrztic()), 0, 10));
        history.setPalletNo(palletNo);
        history.setStatus(BssInventoryHistStatusEnum.TF_P.getCode());
        history.setStorageQty(moveQty);
        history.setUsedQty(moveQty);
        history.setLostDamagedQty(BigDecimal.ZERO);
        history.setTransferNo(transferNo);
        history.setTransferRequestNo(requestNo);
        history.setRequestDatetime(registeredAt);
        history.setRequester(requester);
        history.setVersion(1);
        EntityAuditHelper.fillForInsert(history, now, username, PG_ID_REGISTER);
        bssInventoryMapper.insertHistory(history);
    }

    /**
     * 移動依頼番号を採番する（例：O-261123-001）
     */
    private String generateRequestNo() {
        String date = DateUtils.dateTimeNow(DateUtils.YYYYMMDD).substring(2);
        Integer seq = sysRangedSeqService.increaseSequence(
                SEQ_KEY_TRANSFER_REQUEST, date, null, null, null, null);
        return REQUEST_NO_PREFIX + "-" + date + "-"
                + StringUtils.leftPad(String.valueOf(seq), 3, '0');
    }

    /**
     * 移動番号を採番する（例：M-261123-001）
     */
    private String generateTransferNo() {
        String date = DateUtils.dateTimeNow(DateUtils.YYYYMMDD).substring(2);
        Integer seq = sysRangedSeqService.increaseSequence(
                SEQ_KEY_TRANSFER_MOVE, date, null, null, null, null);
        return MOVE_NO_PREFIX + "-" + date + "-"
                + StringUtils.leftPad(String.valueOf(seq), 3, '0');
    }

    /**
     * カンマ区切りのコードを分ける
     */
    private List<String> splitStatusCodes(String codes) {
        if (StringUtils.isEmpty(codes)) {
            return List.of();
        }
        return Arrays.stream(codes.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .toList();
    }
}
