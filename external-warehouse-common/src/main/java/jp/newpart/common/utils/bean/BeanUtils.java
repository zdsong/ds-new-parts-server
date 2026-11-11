package jp.newpart.common.utils.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean Utils
 * 
 * @author nextosd
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Beanプロパティコピーツールの方法
     * 
     * @param dest
     * @param src
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * オブジェクトのコピー
     * @param source
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T copyBean(Object source, Class<T> clazz) {
        T target = null;
        try {
            target = clazz.newInstance();
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * Listオブジェクトのコピー
     * @param sourceList
     * @param targetClass
     * @return
     * @param <S>
     * @param <T>
     * @param <V>
     */
    public static <S, T, V> List<T> copyBeanList(List<S> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        for (S sourceItem : sourceList) {
            T targetItem = copyBean(sourceItem, targetClass);
            targetList.add(targetItem);
        }
        return targetList;
    }

    /**
     * オブジェクトのsetterメソッドを取得します
     * 
     * @param obj
     * @return
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        List<Method> setterMethods = new ArrayList<Method>();

        Method[] methods = obj.getClass().getMethods();

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }

    /**
     * オブジェクトのgetterメソッドを取得します
     * 
     * @param obj 对象
     * @return 对象的getter方法列表
     */
    public static List<Method> getGetterMethods(Object obj)
    {
        List<Method> getterMethods = new ArrayList<Method>();
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        return getterMethods;
    }

    /**
     * Beanメソッド名の属性名が等しいかどうかをチェックします。<br>
     * getName()とsetName()のプロパティ名のように、getName()とsetAge()のプロパティ名は異なります。
     * 
     * @param m1 メソッド名1
     * @param m2 メソッド名2
     * @return 属性名は同じようにtrueを返し、そうでない場合はfalseを返します
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
