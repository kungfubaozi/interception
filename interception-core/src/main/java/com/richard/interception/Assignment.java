package com.richard.interception;

public class Assignment {

    /**
     * 初始化
     */
    public static void visit() {
        throw new RuntimeException("do you have added 'interception-android' plugin to this module gradle?");
    }

}
