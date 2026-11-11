package jp.newpart.common.utils;

import java.util.*;

public class ValueUtils {

    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;

        } else if (data instanceof String || data instanceof StringBuffer) {
            String str = data.toString().trim();
            return str.isEmpty() || str.equalsIgnoreCase("null");

        } else if (data instanceof Object[]) {
            return ((Object[]) data).length == 0;

        } else if (data instanceof Collection<?>) {
            return ((Collection<?>) data).isEmpty();

        } else if (data instanceof Map<?, ?>) {
            return ((Map<?, ?>) data).isEmpty();
        }

        return false;
    }

    public static boolean isNotEmpty(Object data) {
        return !isEmpty(data);
    }

    public static boolean isEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;

        } else if (a == null || b == null) {
            return false;

        } else if (a.equals(b)) {
            return true;

        } else if (a instanceof List && b instanceof List) {
            List<Object> aList = (List<Object>) a;
            List<Object> bList = (List<Object>) b;

            if (aList.isEmpty() && bList.isEmpty()) {
                return true;
            } else if (aList.size() != bList.size()) {
                return false;
            }

            int i = 0;
            for (Object aObj : aList) {
                if (isNotEqual(aObj, bList.get(i++))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public static boolean isNotEqual(Object a, Object b) {
        return !isEqual(a, b);
    }

    public static boolean isInclude(Object value, Object... checkValues) {
        for (Object checkValue : checkValues) {
            if (isEqual(value, checkValue)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInclude(Object value, String... checkValues) {
        for (Object checkValue : checkValues) {
            if (isEqual(value, checkValue)) {
                return true;
            }
        }

        return false;
    }

    public static Map<String, String> newStringMap(String keys, String... values) {
        Map<String, String> map = new HashMap<String, String>(values == null ? 3 : values.length);

        if (!isEmpty(keys) && !isEmpty(values)) {
            String[] keyArr = keys.split(",");

            if (keyArr.length != values.length) {
                throw new IllegalArgumentException("keys count and values count mismatch!");
            }

            for (int i = 0; i < keyArr.length; i++) {
                map.put(keyArr[i], values[i]);
            }
        }

        return map;
    }

    public static Map<String, Object> merge(Map<String, Object> a, Map<String, Object> b) {
        if (a == null && b == null) {
            return null;
        } else if (a == null && b != null) {
            return b;
        } else if (a != null && b == null) {
            return a;
        } else {
            a.putAll(b);
            return a;
        }
    }

    public static Map<String, Object> newMap(String keys, Object... values) {
        Map<String, Object> map = new HashMap<String, Object>(values == null ? 3 : values.length);

        if (!isEmpty(keys) && !isEmpty(values)) {
            String[] keyArr = keys.split(",");

            if (keyArr.length != values.length) {
                throw new IllegalArgumentException("keys count and values count mismatch!");
            }

            for (int i = 0; i < keyArr.length; i++) {
                map.put(keyArr[i], values[i]);
            }
        }

        return map;
    }

    /**
     * List  values
     *
     * @param values
     * @return
     */
    public static List<Object> newList(Object... values) {
        List<Object> list = new ArrayList<Object>(values == null ? 3 : values.length);
        for (Object value : values) {
            list.add(value);
        }

        return list;
    }

    /**
     * List  values
     *
     * @param values
     * @return
     */
    public static List<String> newStringList(String... values) {
        List<String> list = new ArrayList<String>(values == null ? 3 : values.length);
        for (String value : values) {
            list.add(value);
        }

        return list;
    }

    public static String listToString(List<String> list) {
        return listToString(list, ",");
    }

    public static String listToString(List<String> list, String delimiter) {
        return listToString(list, delimiter, false);
    }


    public static String listToString(List<String> list, String delimiter, boolean removeDuplication) {
        StringJoiner value = new StringJoiner(delimiter);

        if (removeDuplication) {
            Set<String> set = new HashSet<String>();
            for (String str : list)
                set.add(str);

            for (String str : set)
                value.add(str);
        } else {
            for (String str : list)
                value.add(str);
        }

        return value.toString();
    }
}
