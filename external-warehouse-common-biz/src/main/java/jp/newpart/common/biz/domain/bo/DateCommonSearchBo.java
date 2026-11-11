package jp.newpart.common.biz.domain.bo;

import lombok.Data;

@Data
public class DateCommonSearchBo {

    /**
     * 開始日
     */
    private String startDate;

    /**
     * 終了日
     */
    private String endDate;
}
