package com.zskpaco.interception.test.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Richard paco
 * Date: 2018/5/8
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {

}
