package index.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author HaoTian
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    String value();//field name

    boolean participle() default false;//是否分词

    boolean store() default false;//是否存储

    boolean index() default false;//是否索引
}
