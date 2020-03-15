package com.curry.annotation;

import java.lang.annotation.*;

/**
 * 控制器注解
 *
 * @author curry
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurryController {
}
