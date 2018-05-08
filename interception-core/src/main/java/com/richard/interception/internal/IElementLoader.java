package com.richard.interception.internal;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
public interface IElementLoader<T> {

    void init(T local, Object host);

    Object startup(int code, Object[] args);

}
