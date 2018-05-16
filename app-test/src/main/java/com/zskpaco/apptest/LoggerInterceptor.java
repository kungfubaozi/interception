package com.zskpaco.apptest;

import com.zskpaco.interception.AnnotationInterceptor;
import com.zskpaco.interception.Element;
import com.zskpaco.interception.Interceptor;

import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/5/15
 */
@Interceptor
public class LoggerInterceptor implements AnnotationInterceptor<Logger> {
    @Override
    public Object intercept(Element element, Map<String, Object> annotationValues) {
        return null;
    }
}
