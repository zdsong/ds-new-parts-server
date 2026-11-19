package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberEntity;
import jp.nasta.pms.common.biz.enums.BssInventoryStatusEnum;
import jp.nasta.pms.common.biz.enums.BssPrintedFlgEnum;
import jp.nasta.pms.common.biz.enums.BssTransferStatusEnum;
import jp.nasta.pms.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.nasta.pms.common.biz.service.IExternalWarehouseCommonService;
import jp.nasta.pms.common.exception.ServiceException;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssPalletNumberBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssPalletNumberSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletNumberVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletScanLineVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletScanRequestVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssPalletScanVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferOpenLineVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssInventoryMapper;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssPalletNumberMapper;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssTransferOrderMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IBssInventoryService;
import jp.nasta.pms.externalwarehouse.biz.service.IBssPalletNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * パレット番号 Service業務処理
 *
 * @author nextosd
 */
@Service
public class BssPalletNumberServiceImpl implements IBssPalletNumberService {

    /**
     * 登録プログラムＩＤ：パレット番号発番
     */
    private static final String PG_ID_CREATE = ExternalWarehousePgIdEnum.P303.getCode();

    /**
     * 更新プログラムＩＤ：パレット情報詳細
     */
    private static final String PG_ID_UPDATE = ExternalWarehousePgIdEnum.P305.getCode();

    /**
     * 削除フラグ：未削除
     */
    private static final String DELETE_FLG_NORMAL = "0";

    /**
     * 使用可否：使用可
     */
    private static final String USABLE_FLAG_USABLE = "0";

    /**
     * 在庫状況が移動依頼のときの注意
     */
    private static final String MSG_TRANSFER_REQUESTED = "このパレットは既に移動依頼済みです。";

    /**
     * 在庫明細が無いときの注意
     */
    private static final String MSG_INVENTORY_MISSING = "在庫情報が存在しません。";

    @Autowired
    private BssPalletNumberMapper bssPalletNumberMapper;

    @Autowired
    private BssInventoryMapper bssInventoryMapper;

    @Autowired
    private BssTransferOrderMapper bssTransferOrderMapper;

    @Autowired
    private IBssInventoryService bssInventoryService;

    @Autowired
    private IExternalWarehouseCommonService commonService;

    /**
     * パレット番号を検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return パレット番号リスト（積載明細を含む）
     */
    @Override
    public List<BssPalletNumberVo> selectList(BssPalletNumberSearchBo searchBo) {
        List<BssPalletNumberVo> palletList = bssPalletNumberMapper.selectList(searchBo);
        if (CollectionUtils.isEmpty(palletList)) {
            return palletList;
        }

        // 一覧に表示するパレットの積載明細をまとめて取得する（1 パレットずつ取得しない）
        List<String> palletNos = palletList.stream()
                .map(BssPalletNumberVo::getPalletNo)
                .collect(Collectors.toList());
        Map<String, List<BssPalletNumberDetailEntity>> detailMap = bssPalletNumberMapper
                .selectDetailsByPalletNos(palletNos)
                .stream()
                .collect(Collectors.groupingBy(BssPalletNumberDetailEntity::getPalletNo));

        palletList.forEach(pallet -> pallet.setDetails(
                detailMap.getOrDefault(pallet.getPalletNo(), new ArrayList<>())));
        return palletList;
    }

    /**
     * パレット番号を検索
     *
     * @param palletNo パレット番号
     * @return パレット情報（積載明細を含む）
     */
    @Override
    public BssPalletNumberVo selectByPalletNo(String palletNo) {
        BssPalletNumberVo pallet = bssPalletNumberMapper.selectByPalletNo(palletNo);
        if (pallet == null) {
            return null;
        }
        List<String> palletNos = new ArrayList<>();
        palletNos.add(palletNo);
        pallet.setDetails(bssPalletNumberMapper.selectDetailsByPalletNos(palletNos));
        return pallet;
    }

    /**
     * パレット番号が重複していないか確認する
     *
     * @param palletNo パレット番号
     * @return true:重複なし / false:重複あり
     */
    @Override
    public boolean checkPalletNoUnique(String palletNo) {
        return bssPalletNumberMapper.countByPalletNo(palletNo) == 0;
    }

    /**
     * パレット番号を発番し、パレット情報を登録する
     *
     * @param bo パレット番号（積載明細を含む）
     * @return 登録件数（ヘッダ）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPallet(BssPalletNumberBo bo) {
        // パレット番号が未指定の場合は採番する（採番結果は呼び出し元へ返す）
        if (StringUtils.isEmpty(bo.getPalletNo())) {
            bo.setPalletNo(commonService.generatePalletNo());
        }

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();

        // バーコードはパレット番号と同じ値を登録する
        bo.setBarcode(bo.getPalletNo());
        bo.setId(IdUtils.nextIdStr());
        bo.setDeleteFlg(DELETE_FLG_NORMAL);
        // 発番直後は未印刷とする
        bo.setPrintedFlg(BssPrintedFlgEnum.UNPRINTED.getCode());
        bo.setVersion(1);
        bo.setCreateTime(now);
        bo.setCreateBy(username);
        bo.setCreatePgId(PG_ID_CREATE);
        bo.setUpdateTime(now);
        bo.setUpdateBy(username);
        bo.setUpdatePgId(PG_ID_CREATE);
        normalizeHeader(bo);

        int rows = bssPalletNumberMapper.insert(bo);
        insertDetails(bo.getPalletNo(), bo.getDetails(), now, username, PG_ID_CREATE);
        return rows;
    }

    /**
     * パレット情報を更新する（積載明細は入れ替える）
     *
     * @param bo パレット番号（積載明細を含む）
     * @return 更新件数（ヘッダ）。0 の場合は排他エラー
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePallet(BssPalletNumberBo bo) {
        // 印刷済みのパレットは変更できない（番号表を貼り付けた後の改変を防ぐ）
        BssPalletNumberVo current = bssPalletNumberMapper.selectByPalletNo(bo.getPalletNo());
        if (current == null) {
            // パレット番号は存在しません。
            throw new ServiceException(MessageUtils.message("E0016", MessageUtils.message("label.palletNo")));
        }
        if (BssPrintedFlgEnum.PRINTED.getCode().equals(current.getPrintedFlg())) {
            // パレット番号表を印刷済みのため、更新できません。
            throw new ServiceException(MessageUtils.message("E0068"));
        }

        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();

        bo.setUpdateTime(now);
        bo.setUpdateBy(username);
        bo.setUpdatePgId(PG_ID_UPDATE);
        normalizeHeader(bo);

        int rows = bssPalletNumberMapper.update(bo);
        if (rows == 0) {
            // バージョン不一致（他のユーザーが先に更新した）ため、明細は更新しない
            return 0;
        }

        // 画面から外した明細を残さないため、旧明細は削除してから登録し直す
        BssPalletNumberDetailEntity deleteCondition = new BssPalletNumberDetailEntity();
        deleteCondition.setPalletNo(bo.getPalletNo());
        deleteCondition.setUpdateTime(now);
        deleteCondition.setUpdateBy(username);
        deleteCondition.setUpdatePgId(PG_ID_UPDATE);
        bssPalletNumberMapper.deleteDetailsByPalletNo(deleteCondition);

        insertDetails(bo.getPalletNo(), bo.getDetails(), now, username, PG_ID_UPDATE);
        return rows;
    }

    /**
     * パレット情報を論理削除する（積載明細は削除する）
     *
     * @param entity パレット番号
     * @return 削除件数（ヘッダ）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePallet(BssPalletNumberEntity entity) {
        Date now = new Date();
        String username = SecurityUtils.getLoginUser().getUsername();

        entity.setUpdateTime(now);
        entity.setUpdateBy(username);
        entity.setUpdatePgId(PG_ID_UPDATE);

        int rows = bssPalletNumberMapper.deleteLogical(entity);
        if (rows == 0) {
            return 0;
        }

        BssPalletNumberDetailEntity deleteCondition = new BssPalletNumberDetailEntity();
        deleteCondition.setPalletNo(entity.getPalletNo());
        deleteCondition.setUpdateTime(now);
        deleteCondition.setUpdateBy(username);
        deleteCondition.setUpdatePgId(PG_ID_UPDATE);
        bssPalletNumberMapper.deleteDetailsByPalletNo(deleteCondition);
        return rows;
    }

    /**
     * パレット番号表を印刷済みにする。
     * 未印刷の場合は在庫情報詳細と在庫履歴を登録してから印刷フラグを更新する。
     * 印刷済みの場合は在庫を登録せず、現状のままとする。
     *
     * @param palletNo パレット番号
     * @return 更新件数。存在しない場合は 0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePrintedFlg(String palletNo) {
        BssPalletNumberVo current = selectByPalletNo(palletNo);
        if (current == null) {
            return 0;
        }
        // 印刷済みの再印刷では在庫を重複登録しない
        if (BssPrintedFlgEnum.PRINTED.getCode().equals(current.getPrintedFlg())) {
            return 1;
        }

        BssPalletNumberEntity entity = new BssPalletNumberEntity();
        entity.setPalletNo(palletNo);
        entity.setPrintedFlg(BssPrintedFlgEnum.PRINTED.getCode());
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        entity.setUpdatePgId(PG_ID_UPDATE);
        int rows = bssPalletNumberMapper.updatePrintedFlg(entity);
        if (rows == 0) {
            // 同時に印刷済みへ更新された場合は、在庫登録を行わない
            return 1;
        }
        bssInventoryService.registerInboundFromPallet(current);
        return rows;
    }

    /**
     * 移動のバーコード読取結果を組み立てる
     *
     * @param palletNo パレット番号
     * @return 読取結果。パレット番号が無い場合は null
     */
    @Override
    public BssPalletScanVo selectForMoveScan(String palletNo) {
        BssPalletNumberVo pallet = bssPalletNumberMapper.selectByPalletNo(palletNo);
        if (pallet == null) {
            return null;
        }

        List<BssPalletScanLineVo> details = bssInventoryMapper.selectScanLinesByPalletNo(palletNo);
        if (details == null) {
            details = new ArrayList<>();
        }
        boolean transferRequested = details.stream()
                .anyMatch(line -> BssInventoryStatusEnum.TF.getCode().equals(line.getStatus()));

        Map<String, BssTransferOpenLineVo> requestByItem = new LinkedHashMap<>();
        List<BssTransferOpenLineVo> requests = bssTransferOrderMapper.selectOpenByPalletNo(
                palletNo, BssTransferStatusEnum.TF.getCode());
        if (!CollectionUtils.isEmpty(requests)) {
            for (BssTransferOpenLineVo request : requests) {
                requestByItem.putIfAbsent(request.getItemCode(), request);
            }
        }

        List<BssPalletScanLineVo> lines = new ArrayList<>();
        for (BssPalletScanLineVo detail : details) {
            detail.setMoveQty(detail.getStorageQty());
            BssTransferOpenLineVo request = requestByItem.get(detail.getItemCode());
            if (request != null) {
                BssPalletScanRequestVo requestVo = new BssPalletScanRequestVo();
                requestVo.setRequestNo(request.getTransferRequestNo());
                requestVo.setDestinationLabel(StringUtils.isNotEmpty(request.getToWhNm())
                        ? request.getToWhNm() : request.getTransferToWhCd());
                requestVo.setRequester(request.getRequesterName());
                requestVo.setDesiredDate(request.getTransferShipDate());
                requestVo.setRemark(request.getMemo());
                detail.setRequest(requestVo);
                if (request.getStorageQty() != null) {
                    detail.setMoveQty(request.getStorageQty());
                }
            }
            lines.add(detail);
        }

        BssPalletScanVo result = new BssPalletScanVo();
        result.setPalletNo(pallet.getPalletNo());
        result.setTransferRequested(transferRequested);
        result.setLines(lines);
        result.setDependent(lines.stream().anyMatch(line -> line.getRequest() != null));
        if (transferRequested) {
            result.setWarningMessage(MSG_TRANSFER_REQUESTED);
        } else if (lines.isEmpty()) {
            result.setWarningMessage(MSG_INVENTORY_MISSING);
        }
        return result;
    }

    /**
     * 積載明細を登録する
     *
     * @param palletNo パレット番号
     * @param details  積載明細
     * @param now      登録日時
     * @param username 登録者
     * @param pgId     プログラムＩＤ
     */
    private void insertDetails(String palletNo, List<BssPalletNumberDetailEntity> details,
                               Date now, String username, String pgId) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        for (BssPalletNumberDetailEntity detail : details) {
            detail.setId(IdUtils.nextIdStr());
            detail.setPalletNo(palletNo);
            detail.setDeleteFlg(DELETE_FLG_NORMAL);
            detail.setVersion(1);
            detail.setCreateTime(now);
            detail.setCreateBy(username);
            detail.setCreatePgId(pgId);
            detail.setUpdateTime(now);
            detail.setUpdateBy(username);
            detail.setUpdatePgId(pgId);
            if (StringUtils.isEmpty(detail.getUsableFlag())) {
                detail.setUsableFlag(USABLE_FLAG_USABLE);
            }
            bssPalletNumberMapper.insertDetail(detail);
        }
    }

    /**
     * 必須項目（NOT NULL）の未設定値を空文字に揃える
     *
     * @param entity パレット番号
     */
    private void normalizeHeader(BssPalletNumberEntity entity) {
        if (entity.getStoragePeriod() == null) {
            entity.setStoragePeriod(StringUtils.EMPTY);
        }
        if (entity.getMemo1() == null) {
            entity.setMemo1(StringUtils.EMPTY);
        }
        if (entity.getMemo2() == null) {
            entity.setMemo2(StringUtils.EMPTY);
        }
    }
}
