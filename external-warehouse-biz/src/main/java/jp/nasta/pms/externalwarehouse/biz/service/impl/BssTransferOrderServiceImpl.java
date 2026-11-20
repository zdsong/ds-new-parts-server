package jp.nasta.pms.externalwarehouse.biz.service.impl;

import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.BssTransferSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferItemVo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.BssTransferListVo;
import jp.nasta.pms.externalwarehouse.biz.mapper.BssTransferOrderMapper;
import jp.nasta.pms.externalwarehouse.biz.service.IBssTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private BssTransferOrderMapper bssTransferOrderMapper;

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
     * カンマ区切りの移動状況を分ける
     */
    private List<String> splitStatusCodes(String statusCodes) {
        if (StringUtils.isEmpty(statusCodes)) {
            return List.of();
        }
        return Arrays.stream(statusCodes.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .toList();
    }
}
