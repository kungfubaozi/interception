package com.zskpaco.interception.test.interceptor;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Richard paco
 * Date: 2018/5/8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Layout {

    @LayoutRes int value();
    
}
