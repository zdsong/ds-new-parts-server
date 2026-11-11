package jp.newpart.common.utils;

/**
 * 脱感工具類
 *
 * @author nextosd
 */
public class DesensitizedUtil
{
    /**
     * パスワードのすべての文字は*で置換されています。例:*****
     *
     * @param password パスワード
     * @return パスワード
     */
    public static String password(String password)
    {
        if (StringUtils.isBlank(password))
        {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * ナンバープレートの真ん中は*で代用して、間違ったナンバープレートであれば、処理しません
     *
     * @param carLicense ナンバープレート
     * @return ナンバープレート
     */
    public static String carLicense(String carLicense)
    {
        if (StringUtils.isBlank(carLicense))
        {
            return StringUtils.EMPTY;
        }

        if (carLicense.length() == 7)
        {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        }
        else if (carLicense.length() == 8)
        {
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }
}
