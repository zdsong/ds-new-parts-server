package jp.newpart.common.utils;

import jp.newpart.common.annotation.EncryptedField;
import jp.newpart.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * オブジェクト暗号化/復号化ユーティリティクラス
 * アノテーションが付与されたフィールドは、リフレクションを使用して処理されます。
 */
public class ObjectCryptoUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectCryptoUtil.class);

    // 缓存类的字段信息，提高性能
    private static final Map<Class<?>, List<Field>> ENCRYPTED_FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * 暗号化されたオブジェクト内のすべてのフィールドは、@EncryptedField でマークされています
     * @param obj 暗号化対象
     * @param <T> オブジェクトタイプ
     * @return 暗号化されたオブジェクト
     */
    public static <T> T encryptObject(T obj) {
        if (obj == null) {
            return null;
        }

        List<Field> encryptedFields = getEncryptedFields(obj.getClass());

        for (Field field : encryptedFields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value instanceof String && !((String) value).isEmpty()) {
                    EncryptedField annotation = field.getAnnotation(EncryptedField.class);
                    if (annotation.autoEncrypt()) {
                        String encryptedValue = DecryptUtils.encryptAES((String) value);
                        field.set(obj, encryptedValue);
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("暗号化フィールドが失敗しました: {}", field.getName(), e);
                throw new ServiceException(MessageUtils.message("EM007"));
            }
        }

        return obj;
    }

    /**
     * @EncryptedField でマークされたオブジェクト内のすべてのフィールドを復号化します。
     * @param obj 復号化対象
     * @param <T> オブジェクトタイプ
     * @return 復号化されたオブジェクト
     */
    public static <T> T decryptObject(T obj) {
        if (obj == null) {
            return null;
        }

        List<Field> encryptedFields = getEncryptedFields(obj.getClass());

        for (Field field : encryptedFields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value instanceof String && !((String) value).isEmpty()) {
                    EncryptedField annotation = field.getAnnotation(EncryptedField.class);
                    if (annotation.autoDecrypt()) {
                        String decryptedValue = DecryptUtils.decryptAES((String) value);
                        field.set(obj, decryptedValue);
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("フィールドの復号化に失敗しました: {}", field.getName(), e);
                throw new ServiceException(MessageUtils.message("EM008"));
            }
        }

        return obj;
    }

    /**
     * オブジェクトのリストを一括暗号化します
     * @param list オブジェクトのリスト
     * @param <T> オブジェクト型
     * @return 暗号化されたリスト
     */
    public static <T> List<T> encryptList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        return list.stream()
                .map(ObjectCryptoUtil::encryptObject)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * オブジェクトのリストを一括復号します
     * @param list オブジェクトのリスト
     * @param <T> オブジェクト型
     * @return 復号されたリスト
     */
    public static <T> List<T> decryptList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        return list.stream()
                .map(ObjectCryptoUtil::decryptObject)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 指定されたクラス内の、@EncryptedField アノテーションが付与されたすべてのフィールドを取得します
     * @param clazz クラス
     * @return 暗号化されたフィールドのリスト
     */
    private static List<Field> getEncryptedFields(Class<?> clazz) {
        return ENCRYPTED_FIELDS_CACHE.computeIfAbsent(clazz, k -> {
            List<Field> fields = new ArrayList<>();
            ReflectionUtils.doWithFields(k, new FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalAccessException {
                    if (field.isAnnotationPresent(EncryptedField.class)) {
                        if (field.getType() != String.class) {
                            logger.warn("フィールズ {} 型はStringではありません。@EncryptedFieldアノテーションはString型のみをサポートしています", field.getName());
                        } else {
                            fields.add(field);
                        }
                    }
                }
            });
            return Collections.unmodifiableList(fields);
        });
    }

    /**
     * オブジェクトに暗号化が必要なフィールドが含まれているかどうかを確認します
     * @param obj オブジェクト
     * @return 暗号化されたフィールドが含まれているかどうか
     */
    public static boolean hasEncryptedFields(Object obj) {
        if (obj == null) {
            return false;
        }
        List<Field> fields = getEncryptedFields(obj.getClass());
        return !fields.isEmpty();
    }

    /**
     * 指定したフィールドのみを暗号化します（手動で指定）
     * @param obj オブジェクト
     * @param fieldNames 要加密的字段名
     * @param <T> オブジェクトの型
     * @return 暗号化されたオブジェクト
     */
    public static <T> T encryptSpecificFields(T obj, String... fieldNames) {
        if (obj == null || fieldNames == null || fieldNames.length == 0) {
            return obj;
        }

        Set<String> targetFieldNames = new HashSet<>(Arrays.asList(fieldNames));

        List<Field> encryptedFields = getEncryptedFields(obj.getClass());
        for (Field field : encryptedFields) {
            if (targetFieldNames.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if (value instanceof String && !((String) value).isEmpty()) {
                        String encryptedValue = DecryptUtils.encryptAES((String) value);
                        field.set(obj, encryptedValue);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("特定フィールドの暗号化に失敗しました: {}", field.getName(), e);
                }
            }
        }

        return obj;
    }
}
