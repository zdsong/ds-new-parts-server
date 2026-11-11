package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.SysConfig;

import java.util.List;

/**
 * パラメータ設定 サービス層
 *
 * @author nextosd
 */
public interface ISysConfigService {
    /**
     * パラメータ設定情報を検索する
     *
     * @param configId パラメータ設定ID
     * @return パラメータ設定情報
     */
    public SysConfig selectConfigById(String configId);

    /**
     * キー名に基づいてパラメータ設定情報を検索する
     *
     * @param configKey パラメータキー名
     * @return パラメータキー値
     */
    public String selectConfigByKey(String configKey);

    /**
     * キャプチャ機能のオン/オフを取得する
     *
     * @return true: オン, false: オフ
     */
    public boolean selectCaptchaEnabled();

    /**
     * パラメータ設定リストを検索する
     *
     * @param config パラメータ設定情報
     * @return パラメータ設定集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * パラメータ設定を新規追加する
     *
     * @param config パラメータ設定情報
     * @return 結果
     */
    public int insertConfig(SysConfig config);

    /**
     * パラメータ設定を更新する
     *
     * @param config パラメータ設定情報
     * @return 結果
     */
    public int updateConfig(SysConfig config);

    /**
     * パラメータ情報を一括削除する
     *
     * @param configIds 削除する必要があるパラメータID
     */
    public void deleteConfigByIds(String[] configIds);

    /**
     * パラメータキャッシュデータを読み込む
     */
    public void loadingConfigCache();

    /**
     * パラメータキャッシュデータをクリアする
     */
    public void clearConfigCache();

    /**
     * パラメータキャッシュデータをリセットする
     */
    public void resetConfigCache();

    /**
     * パラメータキー名が一意であるかどうかを確認する
     *
     * @param config パラメータ情報
     * @return 結果
     */
    public boolean checkConfigKeyUnique(SysConfig config);
}
