package com.richard.interception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Richard paco
 * Date: 2018/4/27
 * Desc: 包围; 意思是相同字段注解不需要多次执行，把相同的字段包起来，放在一起，然后再顺序执行。
 * 不要写构造方法
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Surround {
}
