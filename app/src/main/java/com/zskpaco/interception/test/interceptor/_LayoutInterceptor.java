package com.zskpaco.interception.test.interceptor;

import com.zskpaco.interception.AnnotationInterceptor;
import com.zskpaco.interception.Element;
import com.zskpaco.interception.Interceptor;
import com.zskpaco.interception.TypeElement;

import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/5/9
 */
@Interceptor
public class _LayoutInterceptor implements AnnotationInterceptor<Layout> {
    @Override
    public Object intercept(Element element, Map<String, Object> annotationValues) {
        if (element instanceof TypeElement) {
            
        }
        return null;
    }
}
