package jp.newpart.common.resolver;

import jp.newpart.common.annotation.DecryptField;
import jp.newpart.common.utils.DecryptUtils;
import jp.newpart.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * デコードフィールド引数解決
 *
 * @author nextosd
 */
@Slf4j
public class DecryptFieldArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * クラスのフィールドをキャッシュして、繰り返しのリフレクション呼び出しを避ける
     */
    private final Map<Class<?>, Field[]> fieldCache = new ConcurrentHashMap<>();

    /**
     * このリゾルバが指定されたメソッドパラメータをサポートするかどうかを判断します。
     *
     * @param parameter メソッドパラメータ
     * @return サポートする場合はtrue、それ以外はfalseを返します
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // フィールドを持つクラス型のパラメータをサポートします
        Class<?> clazz = parameter.getParameterType();
        // クラスに DecryptField アノテーションが付いたフィールドがある場合のみサポートします
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.isAnnotationPresent(DecryptField.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * メソッドパラメータを解析し、DecryptFieldアノテーションが付いたフィールドの値を復号化して設定します。
     *
     * @param parameter     メソッドパラメータ
     * @param mavContainer  ModelAndViewコンテナ
     * @param webRequest    Webリクエスト
     * @param binderFactory WebDataBinderファクトリ
     * @return 解析後のオブジェクト
     * @throws Exception 可能な例外
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> clazz = parameter.getParameterType();

        // パラメータタイプがStringの場合、直接パラメータ値を返します
        if (String.class.equals(clazz)) {
            String paramName = parameter.getParameterName();
            return webRequest.getParameter(paramName);
        }

        // クラスのコンストラクタを取得し、オブジェクトをインスタンス化します
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        try {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            Object obj = constructor.newInstance();

            // クラスのすべてのフィールドを取得します
            Field[] fields = getFields(clazz);
            for (Field field : fields) {
                String value = webRequest.getParameter(field.getName());
                if (value != null) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(DecryptField.class)) {
                        try {
                            String decryptedValue = DecryptUtils.decryptField(value);
                            if (StringUtils.isNotEmpty(decryptedValue)) {
                                field.set(obj, decryptedValue);
                            } else {
                                log.warn("復号化に失敗しました。フィールド: {}, 元の値: {}", field.getName(), value);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            log.error("フィールド値の設定中にエラーが発生しました: {}", field.getName(), e);
                            throw new RuntimeException("フィールド値の設定中にエラーが発生しました: " + field.getName(), e);
                        }
                    } else {
                        field.set(obj, value);
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            log.error("オブジェクトのインスタンス化中にエラーが発生しました: {}", clazz.getName(), e);
            throw new RuntimeException("オブジェクトのインスタンス化中にエラーが発生しました: " + clazz.getName(), e);
        }
    }

    /**
     * 指定されたクラスのフィールドを取得します。キャッシュを使用してパフォーマンスを向上させます。
     *
     * @param clazz クラス
     * @return フィールド配列
     */
    private Field[] getFields(Class<?> clazz) {
        return fieldCache.computeIfAbsent(clazz, c -> c.getDeclaredFields());
    }
}
