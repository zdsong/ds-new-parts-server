package jp.newpart.common.utils;

import jp.newpart.common.utils.spring.SpringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * i 18 nリソースファイルの取得
 * 
 * @author nextosd
 */
public class MessageUtils
{
    /**
     * メッセージ・キーとパラメータに基づいたメッセージの取得spring messageSourceへの委任
     *
     * @param code メッセージキー
     * @param args パラメータ
     * @return 国際化
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
