package jp.newpart.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 浮動小数点演算
 * 
 * @author nextosd
 */
public class Arith
{

    /** デフォルト除算精度 */
    private static final int DEF_DIV_SCALE = 10;

    private Arith(){
    }

    /**
     * 加算。
     * @param v1
     * @param v2
     * @return 和
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 減算
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乗算
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除算
     * 四捨五入
     * @param v1 除算
     * @param v2 除数
     * @return
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 除算 scale
     * 精度を定め、以降の数字を四捨五入する。
     * @param v1
     * @param v2
     * @param scale 精度
     * @return
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        if (b1.compareTo(BigDecimal.ZERO) == 0)
        {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 小数点以下の正確な四捨五入処理を提供
     * @param v 小数
     * @param scale 精度
     * @return
     */
    public static double round(double v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = BigDecimal.ONE;
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }
}
