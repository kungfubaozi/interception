package com.zskpaco.interception;

public class TypeVisitor {

    /**
     * 初始化
     */
    public static void initialize() {
        throw new RuntimeException(
                "do you have added 'interception-android' plugin to this module gradle?");
    }

}
