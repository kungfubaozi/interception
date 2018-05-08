package com.richard.interception.internal;

import com.richard.interception.Element;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/4/26
 */
public interface IElementController {

    Map<String, Object> getAnnotationValue(Class<? extends Annotation> annotation);

    Object processed();

    Element getElement();

    Class[] annotations();

}
