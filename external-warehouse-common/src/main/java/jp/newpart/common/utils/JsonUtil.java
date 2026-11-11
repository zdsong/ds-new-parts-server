package jp.newpart.common.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * JSONユーティリティクラス
 *
 * @author nextosd
 */
@Slf4j
public class JsonUtil {

    /**
     * オブジェクトをJSON文字列に変換する
     *
     * @param obj JSONに変換するオブジェクト
     * @return JSON文字列
     */
    public static String toJsonStr(Object obj) {
        if (obj == null) {
            log.info("Input object is null, returning empty string.");
            return "";
        }
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("Error converting object to JSON string: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    /**
     * JSON文字列をオブジェクトに変換する
     *
     * @param jsonStr JSON文字列
     * @param clazz   オブジェクトの型
     * @return オブジェクトインスタンス
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || jsonStr.isEmpty() || clazz == null) {
            log.warn("Invalid input parameters for parseObject: jsonStr={}, clazz={}", jsonStr, clazz);
            return null;
        }
        try {
            return JSON.parseObject(jsonStr, clazz);
        } catch (JSONException e) {
            log.error("Error parsing JSON string to object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse JSON string to object", e);
        }
    }

    /**
     * JSON文字列をListオブジェクトに変換する
     *
     * @param jsonStr JSON文字列
     * @param clazz   オブジェクトの型
     * @return オブジェクトリスト
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || jsonStr.isEmpty() || clazz == null) {
            log.info("Invalid input parameters for parseArray: jsonStr={}, clazz={}", jsonStr, clazz);
            return null;
        }
        try {
            return JSON.parseArray(jsonStr, clazz);
        } catch (JSONException e) {
            log.info("Error parsing JSON string to list: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse JSON string to list", e);
        }
    }

    /**
     * 文字列が有効なJSONであるかを判定する
     *
     * @param jsonStr 判定する文字列
     * @return JSON文字列である場合はtrue、それ以外はfalse
     */
    public static boolean isJsonStr(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            log.warn("Input string is null or empty, returning false.");
            return false;
        }
        JSONValidator validator = JSONValidator.from(jsonStr);
        return validator.validate();
    }
}