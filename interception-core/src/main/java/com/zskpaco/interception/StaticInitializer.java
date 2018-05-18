package com.zskpaco.interception;

public class StaticInitializer {

    /**
     * 初始化
     */
    public static void justAllOfThis() {
        throw new RuntimeException(
                "do you have added 'interception-android' plugin to this module gradle?");
    }

}
