package com.zskpaco.interception;

/**
 * Author: Richard paco
 * Date: 2018/3/20
 */
public interface ExecutableElement extends Element {

    ParameterElement[] getParameters();

    Class<?> getReturnType();

    Class<?>[] getThrownTypes();

    /**
     * 执行原方法
     *
     * @return
     */
    Object proceed();

}
