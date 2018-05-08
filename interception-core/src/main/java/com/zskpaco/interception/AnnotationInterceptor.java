package com.zskpaco.interception;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
public interface AnnotationInterceptor<T extends Annotation> {

    Object intercept(Element element, Map<String, Object> annotationValues);

}
