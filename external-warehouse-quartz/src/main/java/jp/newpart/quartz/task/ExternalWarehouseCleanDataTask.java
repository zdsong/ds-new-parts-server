package jp.newpart.quartz.task;

import jp.newpart.quartz.service.IExternalWarehouseCleanDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 指定した日数のデータを保持し、その他のデータを削除する
 * 
 * @author nextosd
 */
@Component("farmCleanDataTask")
@Slf4j
public class ExternalWarehouseCleanDataTask
{
    @Autowired
    private IExternalWarehouseCleanDataService farmCleanDataService;

    /**
     * 指定した日数のデータを保持し、その他のデータを削除する
     *
     * 日数より前のデータ削除
     *
     * @param operLogDay       オペレーションログ 日数の指定
     * @param logininforDay    システムアクセスログ 日数の指定
     * @param certificationDay ユーザー認証情報 日数の指定
     * @param jobLogDay        タスクログ 日数の指定
     */
    public void execute(Integer operLogDay, Integer logininforDay, Integer certificationDay, Integer jobLogDay)
    {
        log.debug("指定した日数のデータを保持し、その他のデータを削除するの実行 -- Start --- ");

        farmCleanDataService.clean(operLogDay, logininforDay, certificationDay, jobLogDay);

        log.debug("指定した日数のデータを保持し、その他のデータを削除するの実行 -- End   --- ");
    }
}
