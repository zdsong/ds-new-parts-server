package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.enums.BssInventoryStatusEnum;
import jp.nasta.pms.common.biz.enums.BssTransferStatusEnum;
import jp.nasta.pms.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.nasta.pms.common.core.service.ISysRangedSeqService;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferCandidateSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferOrderDetailBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferCandidateVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferDetailVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssTransferOrderMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IBssTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * 登録プログラムＩＤ：移動依頼（出庫）
     */
    private static final String PG_ID_CREATE = ExternalWarehousePgIdEnum.P401.getCode();

    @Autowired
    private BssTransferOrderMapper bssTransferOrderMapper;

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
            throw new ServiceException("移動する品目を追加してください。");
        }
        if (StringUtils.isEmpty(bo.getDestWhCd())) {
            throw new ServiceException("移動先を選択してください。");
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
            throw new ServiceException("移動依頼番号が指定されていません。");
        }
        if (StringUtils.isEmpty(bo.getDestWhCd())) {
            throw new ServiceException("移動先を選択してください。");
        }
        if (CollectionUtils.isEmpty(bo.getDetails())) {
            throw new ServiceException("移動する品目を追加してください。");
        }

        BssTransferDetailVo current = bssTransferOrderMapper
                .selectTransferDetailByRequestNo(bo.getRequestNo());
        if (current == null) {
            throw new ServiceException("移動依頼が存在しません。");
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
