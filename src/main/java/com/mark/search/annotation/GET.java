package com.mark.search.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 处理get方法
 *
 * @author HaoTian
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GET {
    String path();
}
