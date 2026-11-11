package jp.newpart.common.biz.service.impl;

import jakarta.annotation.PostConstruct;
import jp.newpart.common.annotation.DataSource;
import jp.newpart.common.biz.domain.entity.SysConfig;
import jp.newpart.common.biz.mapper.SysConfigMapper;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.cache.AppCache;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.enums.DataSourceType;
import jp.newpart.common.enums.SysDictTypeEnum;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.text.Convert;
import jp.newpart.common.utils.DictUtils;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * パラメータ設定 サービス層
 *
 * @author nextosd
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private AppCache appCache;

    /**
     * プロジェクトの開始時にキャッシュにパラメータを初期化する
     */
    @PostConstruct
    public void init()
    {
        loadingConfigCache();
    }

    /**
     * システムパラメータ情報の取得
     * 
     * @param configId Id
     * @return システムパラメータ情報
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(String configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * システムパラメータ情報の取得
     * 
     * @param configKey パラメータ key
     * @return パラメータキー値
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        String configValue = Convert.toStr(appCache.getCacheObject(getCacheKey(configKey), String.class));
        if (StringUtils.isNotEmpty(configValue))
        {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig))
        {
            appCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 認証コード取得スイッチ
     *
     * @return trueオープン、falseクローズ
     */
    @Override
    public boolean selectCaptchaEnabled()
    {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled))
        {
            return false;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * パラメータ設定リストを検索する
     *
     * @param config パラメータ設定情報
     * @return パラメータ設定集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        List<SysConfig> sysConfigList = configMapper.selectConfigList(config);
        if(ValueUtils.isNotEmpty(sysConfigList)){
            for(SysConfig sysConfig : sysConfigList){
                sysConfig.setConfigTypeLabel(DictUtils.getDictLabel(SysDictTypeEnum.SYS_YES_NO.getCode(), sysConfig.getConfigType()));
            }
        }
        return sysConfigList;
    }

    /**
     * パラメータ設定を新規追加する
     *
     * @param config パラメータ設定情報
     * @return 結果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        config.setConfigId(IdUtils.nextIdStr());
        int row = configMapper.insertConfig(config);
        if (row > 0)
        {
            appCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * パラメータ設定を更新する
     *
     * @param config パラメータ設定情報
     * @return 結果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey()))
        {
            appCache.deleteObject(getCacheKey(temp.getConfigKey()));
        }

        int row = configMapper.updateConfig(config);
        if (row > 0)
        {
            appCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * パラメータ情報を一括削除する
     *
     * @param configIds 削除する必要があるパラメータID
     */
    @Override
    public void deleteConfigByIds(String[] configIds)
    {
        for (String configId : configIds)
        {
            SysConfig config = selectConfigById(configId);

            // システム組み込み = Y, 削除できません
            if(ValueUtils.isEqual(config.getConfigType(), "Y")){
                throw new ServiceException(MessageUtils.message("E0006"));
            }

            configMapper.deleteConfigById(configId);
            appCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * パラメータキャッシュデータのロード
     */
    @Override
    public void loadingConfigCache()
    {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList)
        {
            appCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * パラメータキャッシュデータの消去
     */
    @Override
    public void clearConfigCache()
    {
        Collection<String> keys = appCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        appCache.deleteObject(keys);
    }

    /**
     * パラメータキャッシュデータをリセットするには
     */
    @Override
    public void resetConfigCache()
    {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * パラメータキー名が一意であることを確認します
     * 
     * @param config システムパラメータ情報
     * @return
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config)
    {
        String configId = config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && !info.getConfigId().equals(configId))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * cache keyの設定
     * 
     * @param configKey パラメータキー
     * @return キャッシュキーkey
     */
    private String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
