package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.SysConfig;

import java.util.List;

/**
 * システムパラメータ 数据层
 * 
 * @author nextosd
 */
public interface SysConfigMapper
{
    /**
     *  システムパラメータ情報の取得
     * 
     * @param config システムパラメータ情報
     * @return システムパラメータ情報
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * IDによるシステムパラメータ情報の検索
     * 
     * @param configId Id
     * @return システムパラメータ情報
     */
    public SysConfig selectConfigById(String configId);

    /**
     * システムパラメータ情報の取得
     * 
     * @param config システムパラメータ情報
     * @return システムパラメータ情報
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * キー名によるシステムパラメータ情報の検索
     * 
     * @param configKey パラメータキー名
     * @return システムパラメータ情報
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * システムパラメータ情報の追加
     * 
     * @param config システムパラメータ情報
     * @return
     */
    public int insertConfig(SysConfig config);

    /**
     * システムパラメータ情報の変更
     * 
     * @param config システムパラメータ情報
     * @return
     */
    public int updateConfig(SysConfig config);

    /**
     * システムパラメータ情報の削除
     * 
     * @param configId ID
     * @return
     */
    public int deleteConfigById(String configId);

    /**
     * 一括削除情報
     * 
     * @param configIds Ids
     * @return
     */
    public int deleteConfigByIds(String[] configIds);
}
