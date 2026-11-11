package jp.newpart.common.utils;

import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.cache.AppCache;
import jp.newpart.common.utils.spring.SpringUtils;

/**
 * 辞書ツールクラス
 * 
 * @author nextosd
 */
public class DictUtils
{
    /**
     * 区切り文字
     */
    public static final String SEPARATOR = ",";

    /**
     * 辞書キャッシュの設定
     * 
     * @param key パラメータキー
     * @param dictDatas 辞書データリスト
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(AppCache.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 辞書キャッシュの取得
     * 
     * @param key パラメータキー
     * @return dictDatas 辞書データリスト
     */
    public static List<SysDictData> getDictCache(String key)
    {
        JSONArray arrayCache = SpringUtils.getBean(AppCache.class).getCacheObject(getCacheKey(key), JSONArray.class);
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * 辞書タイプと辞書値に基づいた辞書ラベルの取得
     * 
     * @param dictType 辞書の種類
     * @param dictValue 辞書の値
     * @return 辞書ラベル
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        if (StringUtils.isEmpty(dictValue))
        {
            return StringUtils.EMPTY;
        }
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 辞書タイプと辞書ラベルに基づいた辞書値の取得
     * 
     * @param dictType 辞書の種類
     * @param dictLabel 辞書の値
     * @return 辞書ラベル
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        if (StringUtils.isEmpty(dictLabel))
        {
            return StringUtils.EMPTY;
        }
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 辞書タイプと辞書値に基づいた辞書ラベルの取得
     * 
     * @param dictType 辞書の種類
     * @param dictValue 辞書の値
     * @param separator 区切り文字
     * @return 辞書ラベル
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictValue))
        {
            for (SysDictData dict : datas)
            {
                for (String value : dictValue.split(separator))
                {
                    if (value.equals(dict.getDictValue()))
                    {
                        propertyString.append(dict.getDictLabel()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictValue.equals(dict.getDictValue()))
                {
                    return dict.getDictLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType 辞書の種類
     * @param dictLabel 辞書ラベル
     * @param separator 区切り文字
     * @return 辞書の値
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        if (StringUtils.containsAny(separator, dictLabel))
        {
            for (SysDictData dict : datas)
            {
                for (String label : dictLabel.split(separator))
                {
                    if (label.equals(dict.getDictLabel()))
                    {
                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictLabel.equals(dict.getDictLabel()))
                {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 辞書タイプに基づいて辞書のすべての値を取得する
     *
     * @param dictType 辞書の種類
     * @return 辞書の値
     */
    public static String getDictValues(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictValue()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 辞書タイプに基づいて辞書のすべてのラベルを取得する
     *
     * @param dictType 辞書の種類
     * @return 辞書の値
     */
    public static String getDictLabels(String dictType)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas))
        {
            return StringUtils.EMPTY;
        }
        for (SysDictData dict : datas)
        {
            propertyString.append(dict.getDictLabel()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 指定されたディクショナリキャッシュの削除
     * 
     * @param key 辞書キー
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(AppCache.class).deleteObject(getCacheKey(key));
    }

    /**
     * 辞書キャッシュを空にする
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(AppCache.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(AppCache.class).deleteObject(keys);
    }

    /**
     * cache keyの設定
     * 
     * @param configKey パラメータキー
     * @return キャッシュキーkey
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
