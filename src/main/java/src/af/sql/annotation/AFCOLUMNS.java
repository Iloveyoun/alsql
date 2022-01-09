package src.af.sql.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/* 简洁描述多列
 *
 */

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface AFCOLUMNS {
    boolean auto() default true; // 是否自动从field中提取

    String generated() default ""; // 自增主键的列名

    String names() default "";     // 手工指定列名 ,暂不支持

}
