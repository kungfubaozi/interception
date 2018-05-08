package com.zskpaco.interception;

import java.lang.annotation.*;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Interceptor {

    Environment environment() default Environment.RELEASE;

    boolean singleton() default false;

    Class<? extends Annotation>[] coupling() default {};

}
