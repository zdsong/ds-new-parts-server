package jp.newpart.common.biz.utils;

import jp.newpart.common.biz.constants.ExternalWarehouseConstants;

public class ExternalWarehouseUtils {

    /**
     * ユーザー名を連結する
     *
     *
     * @return Date() 現在日付
     */
    public static String concatUsername(String firstNm, String lastNm)
    {
        StringBuilder customerNmBuilder;
        customerNmBuilder = new StringBuilder();
        customerNmBuilder.append(firstNm);
        customerNmBuilder.append(ExternalWarehouseConstants.HALF_WIDTH_SPACE);
        customerNmBuilder.append(lastNm);
        return  customerNmBuilder.toString();
    }
}
