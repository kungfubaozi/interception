package com.richard.interception.internal;

import java.lang.annotation.Annotation;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
public interface IElementMultipartBuilder {


    IElementMultipartBuilder setTypeNames(String[] names);

    IElementMultipartBuilder addAnnotationToPosition(int position,
                                                     Class<? extends Annotation>[] annotations);

    IElementMultipartBuilder addTypesAnnotationValues(int position,
                                                      Class<? extends Annotation> annotation,
                                                      String key, Object value);

    IElementMultipartBuilder setHost(Object host);

    IElementController generate(IElementExecutionLoader buildLoader);

}
