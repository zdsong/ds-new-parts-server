package jp.newpart.common.mybatis.interceptor;


import jp.newpart.common.annotation.EncryptedField;
import jp.newpart.common.core.domain.BaseEntity;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.utils.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * MyBatis 汎用データベースインターセプター
 * このインターセプターは、INSERT または UPDATE 操作を実行する際に、作成日時、更新日時、作成ユーザー、更新ユーザーなどのフィールドを自動的に設定します。
 *
 * @author nextosd
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MyBatisInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisInterceptor.class);

    private static final String CREATE_BY = "createBy";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_TIME = "updateTime";

    /**
     * MyBatis の update 操作をインターセプトし、関連フィールドを自動的に設定します。
     *
     * @param invocation インターセプトされたメソッドの情報を含む
     * @return 実行結果
     * @throws Throwable 実行中に例外が発生した場合
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];

        if(parameterObject == null){
            // 元のメソッドを続行
            return invocation.proceed();
        }

        Class classParameter = parameterObject.getClass();
        Field[] fields = classParameter.getDeclaredFields();

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // 現在の日時と現在のユーザーIDを取得
            Date currentDate = DateUtils.getNowDate();
            String currentUser = this.getLoginUserId();

            // パラメータオブジェクトが BaseEntity 型の場合、直接関連フィールドを設定
            if (parameterObject instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) parameterObject;
                if (sqlCommandType == SqlCommandType.INSERT) {
                    // INSERT 操作の場合、作成日時と作成ユーザーを設定
                    entity.setCreateBy(currentUser);
                    entity.setCreateTime(currentDate);
                    entity.setUpdateBy(currentUser);
                    entity.setUpdateTime(currentDate);
                } else if (sqlCommandType == SqlCommandType.UPDATE) {
                    // UPDATE 操作の場合、更新日時と更新ユーザーを設定
                    entity.setUpdateBy(currentUser);
                    entity.setUpdateTime(currentDate);
                }
            } else {
                // パラメータオブジェクトが BaseEntity 型でない場合、リフレクションを使用して関連フィールドを設定
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();

                    switch (fieldName) {
                        case CREATE_BY:
                            if (SqlCommandType.INSERT.equals(sqlCommandType) && StringUtils.isNotEmpty(currentUser)) {
                                field.set(parameterObject, currentUser);
                            }
                            break;
                        case CREATE_TIME:
                            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                                field.set(parameterObject, currentDate);
                            }
                            break;
                        case UPDATE_BY:
                            if (StringUtils.isNotEmpty(currentUser)) {
                                field.set(parameterObject, currentUser);
                            }
                            break;
                        case UPDATE_TIME:
                            field.set(parameterObject, currentDate);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        encryptFields(parameterObject, classParameter, sqlCommandType);

        // 元のメソッドを続行
        return invocation.proceed();
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
     * 現在のログインユーザーのIDを取得します。
     * 認証情報が取得できない場合は、デフォルト値 "System" を返します。
     *
     * @return 現在のログインユーザーのID
     */
    private String getLoginUserId() {
        String loginUserId = "System";
        try {
            Authentication authentication = SecurityUtils.getAuthentication();
            if (authentication == null) {
                return loginUserId;
            }
            Object principal = authentication.getPrincipal();
            if (principal != null && principal instanceof LoginUser) {
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                if (loginUser != null) {
                    loginUserId = loginUser.getUsername();
                }
            }
            return loginUserId;
        } catch (Exception ex) {
            logger.warn("ログインユーザーIDの取得に失敗しました", ex);
            return loginUserId;
        }
    }

    /**
     * @EncryptedField アノテーションが付いたフィールドを暗号化する
     */
    private void encryptFields(Object parameterObject, Class<?> clazz,
                               SqlCommandType sqlCommandType)
            throws IllegalAccessException {

        // すべてのフィールドを取得する
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // @EncryptedField アノテーションが存在するかどうかを確認してください
            EncryptedField encryptedField = field.getAnnotation(EncryptedField.class);
            if (encryptedField == null) {
                continue;
            }

            // 自動暗号化が必要かどうかを確認してください。
            if (!encryptedField.autoEncrypt()) {
                continue;
            }

            // フィールドタイプをチェックする
            if (field.getType() != String.class) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(parameterObject);

            // 値が空の場合はスキップします。
            if (value == null) {
                continue;
            }

            String stringValue = (String) value;
            if (stringValue.isEmpty()) {
                continue;
            }

            // AES 暗号化
            String encryptedValue = DecryptUtils.encryptAES(stringValue);
            field.set(parameterObject, encryptedValue);
        }

        // 親クラスのフィールドを再帰的に処理する
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            encryptFields(parameterObject, superClass, sqlCommandType);
        }
    }
}