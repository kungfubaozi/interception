package com.zskpaco.interception.internal;

import java.lang.annotation.Annotation;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
public interface IElementBuilder extends IElementMultipartBuilder {

    IElementMultipartBuilder setType(boolean async, boolean ui);

    boolean async();

    boolean ui();

    IElementBuilder setArgs(Object[] args);

    IElementMultipartBuilder setAnnotation(Class<? extends Annotation>[] annotations);

    IElementBuilder addAnnotationValues(Class<? extends Annotation> annotation, String key,
                                        Object value);

    IElementBuilder setThrownTypes(Class<?>[] thrownTypes);


}
