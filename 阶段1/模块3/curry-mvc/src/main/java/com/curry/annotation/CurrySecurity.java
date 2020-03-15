package com.curry.annotation;

import java.lang.annotation.*;

/**
 * 权限注解，有哪些用户可以请求该接口
 *
 * @author curry
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrySecurity {
    /**
     * 具体的用户名称
     *
     * @return
     */
    String[] value() default "";
}
