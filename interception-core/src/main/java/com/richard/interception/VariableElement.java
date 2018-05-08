package com.richard.interception;

/**
 * Author: Richard paco
 * Date: 2018/3/20
 */
public interface VariableElement extends Element {

    Class<?> getVariableType();

    Object getValue();

    void setValue(Object value);

}
