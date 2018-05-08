package com.zskpaco.interception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Richard paco
 * Date: 2018/3/19
 * Desc:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Async {
}
