package com.zskpaco.apptest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Richard paco
 * Date: 2018/5/15
 * Desc:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Logger {
}
