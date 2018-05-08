package com.richard.interception.internal;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 * Desc:
 */
public interface IElementExecutionLoader {

    /**
     * @param processId
     * @param constant  对Variable赋值
     * @param args
     * @return
     */
    Object build(int processId, boolean constant, Object[] args);

}
