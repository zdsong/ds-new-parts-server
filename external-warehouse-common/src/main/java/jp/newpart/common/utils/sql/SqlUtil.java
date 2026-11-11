package jp.newpart.common.utils.sql;

import jp.newpart.common.exception.UtilException;
import jp.newpart.common.utils.StringUtils;

/**
 * sql操作ツール類
 * 
 * @author nextosd
 */
public class SqlUtil
{
    /**
     * sqlキーワードの定義
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|sleep|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |union |like |+|/*|user()";

    /**
     * 文字、数字、アンダースコア、スペース、カンマ、小数点のみをサポート（複数のフィールドのソートをサポート）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 制限orderBy最大長さ
     */
    private static final int ORDER_BY_MAX_LENGTH = 500;

    /**
     * 文字をチェックして、インジェクションがバイパスされないようにする
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("パラメータが仕様に一致せず、クエリを実行できません");
        }
        if (StringUtils.length(value) > ORDER_BY_MAX_LENGTH)
        {
            throw new UtilException("パラメータが最大制限を超えており、クエリを実行できません");
        }
        return value;
    }

    /**
     * order by構文が仕様に合致していることを確認します
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQLキーワードチェック
     */
    public static void filterKeyword(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords)
        {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1)
            {
                throw new UtilException("パラメータにSQLインジェクションのリスクがある");
            }
        }
    }
}
