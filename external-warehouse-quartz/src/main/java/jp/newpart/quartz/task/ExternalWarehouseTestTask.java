package jp.newpart.quartz.task;

import jp.newpart.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * スケジュールタスクスケジューリングテスト
 * 
 * @author nextosd
 */
@Component("farmTestTask")
@Slf4j
public class ExternalWarehouseTestTask
{
    public void testMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        log.info(StringUtils.format("マルチパラメータメソッドの実行： String{}，Boolean{}，Long{}，Double{}，Integer{}", s, b, l, d, i));
    }

    public void testParams(String params)
    {
        log.info("参照メソッドの実行：" + params);
    }

    public void testNoParams()
    {
        log.info("参照なしメソッドの実行");
    }
}
