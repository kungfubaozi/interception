package com.richard.interception.internal;

import com.richard.interception.AnnotationInterceptor;

import java.lang.annotation.Annotation;

/**
 * Author: Richard paco
 * Date: 2018/4/27
 */
public interface IElementInstanceLoader {

    AnnotationInterceptor getInterceptorInstance(Class<? extends Annotation> annotation);

}
