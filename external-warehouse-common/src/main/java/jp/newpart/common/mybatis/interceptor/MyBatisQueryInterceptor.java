package jp.newpart.common.mybatis.interceptor;

import jp.newpart.common.utils.ObjectCryptoUtil;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * MyBatis 汎用データベースインターセプター
 *
 * @author nextosd
 */
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class,
                    ResultHandler.class, CacheKey.class, BoundSql.class}
    )
})
public class MyBatisQueryInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisQueryInterceptor.class);

    /**
     * MyBatis の query に設定します。
     *
     * @param invocation インターセプトされたメソッドの情報を含む
     * @return 実行結果
     * @throws Throwable 実行中に例外が発生した場合
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // パラメータを表示
        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        MappedStatement ms = (MappedStatement) args[0];
        String sql = ms.getBoundSql(parameter).getSql().toLowerCase();
        if (ValueUtils.isNotEmpty(sql) && sql.contains(" like " )) {
            // パラメータエスケープ
            processParameterEscape(parameter);
        }

        // 元のメソッドを続行
        decryptResult(invocation.proceed());
        return invocation.proceed();
    }

    /**
     * パラメータエスケープ
     */
    private void processParameterEscape(Object parameter) {
        if (parameter == null) {
            return;
        }

        if (!(parameter instanceof Map)) {
            // オブジェクトパラメータの処理
            handleObjectParameter(parameter);
        }
    }

    /**
     * マップ型パラメータの処理
     */
    private void handleMapParameter(Map<String, Object> paramMap) {
        paramMap.forEach((key, value) -> {
            // すべての文字列値をエスケープするか、必要に応じて特定のキーのチェックを追加します。
            if (value instanceof String && StringUtils.hasText((String) value)) {
                String escapedValue = escapeLikeValue((String) value);
                paramMap.put(key, escapedValue);
            }
        });
    }

    /**
     * オブジェクトパラメータの処理
     */
    private void handleObjectParameter(Object parameter) {
        try {
            Field[] fields = parameter.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 文字列型フィールドのみ処理する
                if (field.getType().equals(String.class)) {
                    field.setAccessible(true);
                    Object value = field.get(parameter);
                    if (value instanceof String && StringUtils.hasText((String) value)) {
                        String escapedValue = escapeLikeValue((String) value);
                        field.set(parameter, escapedValue);
                    }
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            // アクセス例外を無視し、既存のパラメータを変更しません。
            System.err.println("フィールドアクセスに失敗しました: " + e.getMessage());
        }
    }

    /**
     * LIKEクエリで特殊文字をエスケープする
     */
    private String escapeLikeValue(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        return value.replace("\\", "\\\\")
                .replace("%", "\\%");
//                .replace("_", "\\_");
    }

    /**
     * 現在のインターセプターを MyBatis プラグインとしてラップします。
     *
     * @param target インターセプトされたオブジェクト
     * @return ラップされたオブジェクト
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * インターセプターのプロパティを設定します。
     *
     * @param properties プロパティの集合
     */
    @Override
    public void setProperties(Properties properties) {
        // ここでインターセプターのプロパティを設定できます
    }

    /**
     * クエリ結果を復号化する
     */
    private void decryptResult(Object result) {
        if (result == null) {
            return;
        }

        // 処理リスト
        if (result instanceof List) {
            List<?> resultList = (List<?>) result;
            if (!CollectionUtils.isEmpty(resultList)) {
                for (Object item : resultList) {
                    if (item != null) {
                        ObjectCryptoUtil.decryptObject(item);
                    }
                }
            }
        }
        // 単一オブジェクトの処理
        else if (hasEncryptedFields(result.getClass())) {
            ObjectCryptoUtil.decryptObject(result);
        }
    }

    /**
     * クラスに暗号化されたフィールドがあるかどうかを判定する
     */
    private boolean hasEncryptedFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(jp.newpart.common.annotation.EncryptedField.class)) {
                return true;
            }
        }
        return false;
    }
}