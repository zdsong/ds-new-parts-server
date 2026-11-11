package jp.newpart.quartz.service.impl;

import jp.newpart.common.utils.DateUtils;
import jp.newpart.quartz.mapper.SysCleanDataMapper;
import jp.newpart.quartz.service.IExternalWarehouseCleanDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * システムに不要なデータを消去する
 * 日数より前のデータ削除
 */
@Slf4j
@Service
public class ExternalWarehouseCleanDataServiceImpl implements IExternalWarehouseCleanDataService {

    @Autowired
    private SysCleanDataMapper sysCleanDataMapper;

    /**
     * システムに不要なデータを消去する
     * 日数より前のデータ削除
     *
     * @param operLogDay       オペレーションログ 日数の指定
     * @param logininforDay    システムアクセスログ 日数の指定
     * @param certificationDay ユーザー認証情報 日数の指定
     * @param jobLogDay        タスクログ 日数の指定
     * @return
     */
    public int clean(int operLogDay, int logininforDay, int certificationDay, int jobLogDay){

        int result = 0;

        Date nowDate = DateUtils.getNowDate();

        String operLogPrevDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -operLogDay));
        int delCnt = sysCleanDataMapper.deleteSysOperLogByDate(operLogPrevDate);
        result = result + delCnt;
        log.info("オペレーションログデータ削除 指定日数 = " + operLogDay + " 日付より前 = " + operLogPrevDate + " レコード数 = " + delCnt);

        String logininforPrevDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -logininforDay));
        delCnt = sysCleanDataMapper.deleteSysLogininforByDate(logininforPrevDate);
        result = result + delCnt;
        log.info("システムアクセスログデータ削除 指定日数 = " + logininforDay + " 日付より前 = " + logininforPrevDate + " レコード数 = " + delCnt);

        String jobLogPrevDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -jobLogDay));
        delCnt = sysCleanDataMapper.deleteJobLogByDate(jobLogPrevDate);
        result = result + delCnt;
        log.info("タスクJOBログデータ削除 指定日数 = " + jobLogDay + " 日付より前 = " + jobLogPrevDate + " レコード数 = " + delCnt);

        String userCertificationPrevDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -certificationDay));
        delCnt = sysCleanDataMapper.deleteBssUserCertificationByDate(userCertificationPrevDate);
        log.info("ユーザー認証情報データ削除 指定日数 = " + certificationDay + " 日付より前 = " + userCertificationPrevDate + " レコード数 = " + delCnt);
        result = result + delCnt;

        return result;
    }
}
