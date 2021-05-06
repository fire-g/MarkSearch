package com.mark.search.index.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引注解,用于标注实体是否分词、是否存储、是否索引以及标识索引片名称
 *
 * @author HaoTian
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    //field name
    String value();

    //是否分词
    boolean participle() default false;

    //是否存储
    boolean store() default false;

    //是否索引
    boolean index() default false;
}
