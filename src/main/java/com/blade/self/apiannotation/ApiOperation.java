package com.blade.self.apiannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在指定的路径上，对一个操作或HTTP方法进行描述.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiOperation {

    String value() default "";
    String note() default "ApiOperation 注解";
    String httpMethod() default "";
    String code() default "200";

}
