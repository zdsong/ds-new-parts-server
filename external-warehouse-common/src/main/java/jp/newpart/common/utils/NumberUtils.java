package jp.newpart.common.utils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 数値処理クラス
 */
public class NumberUtils {

    /**
     * 2つのBigDecimal型を追加する
     * @param decimal1 データ1
     * @param decimal2 データ2
     * @return 結果
     */
    public static BigDecimal addBigDecimal(BigDecimal decimal1, BigDecimal decimal2) {
        return Optional.ofNullable(decimal1)
                .orElse(BigDecimal.ZERO)
                .add(Optional.ofNullable(decimal2)
                        .orElse(BigDecimal.ZERO));
    }
}
