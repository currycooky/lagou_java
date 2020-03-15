package com.curry.annotation;

import java.lang.annotation.*;

/**
 * 请求url注解
 *
 * @author curry
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurryRequestMapping {
    /**
     * 配置请求的url地址
     *
     * @return
     */
    String value() default "";
}
