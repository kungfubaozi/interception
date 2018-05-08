package com.richard.interception;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/3/20
 */
public interface ParameterElement extends Element {

    /**
     * 获取类型
     *
     * @return
     */
    Class<?> getVariableType();

    Object getValue();

    /**
     * 获取注解信息
     *
     * @param annotation
     * @return
     */
    Map<String, Object> getAnnotation(Class<? extends Annotation> annotation);

}
