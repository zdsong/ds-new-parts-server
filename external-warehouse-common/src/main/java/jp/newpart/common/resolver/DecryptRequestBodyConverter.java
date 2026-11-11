package jp.newpart.common.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.newpart.common.annotation.DecryptField;
import jp.newpart.common.utils.DecryptUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * リクエストボディの復号化コンバーター
 * このコンバーターはMappingJackson2HttpMessageConverterを継承し、DecryptFieldアノテーションが付与されたフィールドの復号化を処理します。
 * 暗号化されたリクエストボディを受け取った後、DecryptUtilsユーティリティクラスを使用して指定されたフィールドを復号化します。
 *
 * @author nextosd
 */
public class DecryptRequestBodyConverter extends MappingJackson2HttpMessageConverter {
    private final Map<Class<?>, Field[]> cachedFields = new HashMap<>();

    /**
     * コンストラクタ。ObjectMapperインスタンスを受け取ります。
     *
     * @param objectMapper JacksonのObjectMapperインスタンス。JSONのシリアライズとデシリアライズに使用されます。
     */
    public DecryptRequestBodyConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    /**
     * readメソッドをオーバーライドし、リクエストボディ内のフィールドを復号化します。
     * 親クラスのreadメソッドを呼び出した後、フィールドの復号化ロジックを実行します。
     *
     * @param type         リクエストボディの型
     * @param contextClass コンテキストクラス。フィールドの解析に使用されます。
     * @param inputMessage HTTP入力メッセージ。リクエストボディを含みます。
     * @return 復号化されたオブジェクトインスタンス
     * @throws IOException                     I/Oエラーが発生した場合
     * @throws HttpMessageNotReadableException HTTPメッセージが読み取れない場合
     */
    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        Object obj = super.read(type, contextClass, inputMessage);
        decryptFields(obj);
        return obj;
    }

    /**
     * オブジェクトのすべてのフィールドを反復処理し、DecryptFieldアノテーションが付与されたフィールドを復号化します。
     *
     * @param obj 復号化するフィールドを持つオブジェクトインスタンス
     */
    private void decryptFields(Object obj) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = cachedFields.computeIfAbsent(clazz, k -> getAnnotatedFields(k));

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value instanceof String) {
                    String decryptedValue = DecryptUtils.decryptField((String) value);
                    field.set(obj, decryptedValue);
                }
                field.setAccessible(false); // Restore accessibility
            } catch (IllegalAccessException | RuntimeException e) {
                throw new DecryptionException("Failed to decrypt field: " + field.getName(), e);
            }
        }
    }

    /**
     * DecryptFieldアノテーションが付与されたフィールドを取得します
     *
     * @param clazz クラス
     * @return DecryptFieldアノテーションが付与されたフィールドの配列
     */
    private Field[] getAnnotatedFields(Class<?> clazz) {
        return java.util.Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DecryptField.class))
                .toArray(Field[]::new);
    }

    /**
     * 自定义解密异常类
     */
    public static class DecryptionException extends RuntimeException {
        public DecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
