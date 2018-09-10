package com.blade.self.apiannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路径操作的返回值的注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiResponse {
    String code() default "505";
    String message() default "ApiResponse 注解";
    String response() default "";
    String responseContainer() default  "";
}
