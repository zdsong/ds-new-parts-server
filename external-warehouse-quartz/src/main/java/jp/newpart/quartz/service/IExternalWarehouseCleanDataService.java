package jp.newpart.quartz.service;

/**
 * システムに不要なデータを消去する
 */
public interface IExternalWarehouseCleanDataService {

    /**
     * システムに不要なデータを消去する
     * 日数より前のデータ削除
     *
     * @param operLogDay       オペレーションログ 日数の指定
     * @param logininforDay    システムアクセスログ 日数の指定
     * @param certificationDay ユーザー認証情報 日数の指定
     *  @param jobDay          タスクログ 日数の指定
     * @return
     */
    int clean(int operLogDay, int logininforDay, int certificationDay, int jobDay);
}
