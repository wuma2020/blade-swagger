package com.blade.self.apiannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

/**
 * 给controller类的注解
 *  声明该类的方法需要解析成
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Api {

    String tags() default "";

    String value() default "";

    String description() default "";

}
