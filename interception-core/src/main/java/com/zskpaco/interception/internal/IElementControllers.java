package com.zskpaco.interception.internal;

import com.zskpaco.interception.Element;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Author: Richard paco
 * Date: 2018/4/26
 */
public interface IElementControllers extends IElementController {

    Element getElementByIndex(int index);

    Map<Class<? extends Annotation>, Map<String, Object>> getAnnotationInfoByIndex(int index);

    int size();

}
