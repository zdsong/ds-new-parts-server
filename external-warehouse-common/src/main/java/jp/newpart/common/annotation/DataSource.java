package jp.newpart.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jp.newpart.common.enums.DataSourceType;

/**
 * カスタムマルチデータソース切り替え注記
 *
 * 優先度：先行メソッド、後クラス、メソッドがクラス上のデータソースタイプを上書きしている場合、
 *        メソッドに準拠している場合、そうでない場合、クラス上の準拠
 *
 * @author nextosd
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource
{
    /**
     * データソース名の切り替え
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
