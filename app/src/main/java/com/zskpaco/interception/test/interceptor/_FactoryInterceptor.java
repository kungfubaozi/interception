package com.zskpaco.interception.test.interceptor;

import com.zskpaco.interception.AnnotationInterceptor;
import com.zskpaco.interception.Element;
import com.zskpaco.interception.Interceptor;
import com.zskpaco.interception.VariableElement;
import com.zskpaco.interception.test.data.UserInfo;

import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/5/9
 */
@Interceptor(singleton = true)
public class _FactoryInterceptor implements AnnotationInterceptor<Factory> {
    @Override
    public Object intercept(Element element, Map<String, Object> annotationValues) {
        if (element instanceof VariableElement) {
            Class<?> type = ((VariableElement) element).getVariableType();
            if (type.equals(UserInfo.class)) {
                ((VariableElement) element).setValue(new UserInfo());
            }
        }
        return null;
    }
}
