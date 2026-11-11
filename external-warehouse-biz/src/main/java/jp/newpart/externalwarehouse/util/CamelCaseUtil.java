package jp.newpart.externalwarehouse.biz.util;

public class CamelCaseUtil {

    /**
     * 下線で区切られた文字列をラクダピーク形式に変換する（頭文字小文字）
     * 例：hello_world → helloWorld
     *
     * @param str
     * @return ラクダピーク文字列
     */
    public static String toLowerCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] parts = str.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase()); // 第一个单词保持原样（小写）
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1).toLowerCase());
            }
        }
        return camelCase.toString();
    }

    /**
     *下線で区切られた文字列を大ラクダピーク形式に変換する（頭文字大文字）
     *例：hello _ world→HelloWorld
     *
     *@param str
     *@return ラクダピーク文字列
     */
    public static String toUpperCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] parts = str.split("_");
        StringBuilder camelCase = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                camelCase.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }
        return camelCase.toString();
    }
}