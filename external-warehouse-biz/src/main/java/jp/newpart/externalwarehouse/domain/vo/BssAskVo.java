package jp.newpart.externalwarehouse.biz.domain.vo;


import lombok.Data;

import java.util.List;

/**
 * 問合せ情報詳細情報Vo
 *
 * @author nextosd
 */
@Data
public class BssAskVo {

    /**
     * ID
     */
    private String id;

    /**
     * お問い合わせ番号
     */
    private String askNo;

    /**
     * 問合せ受付日
     */
    private String askDate;

    /**
     * 問合せ対応履歴情報 詳細情報Vo
     */
    private List<BssAskHistoryVo> askHistoryList;
}