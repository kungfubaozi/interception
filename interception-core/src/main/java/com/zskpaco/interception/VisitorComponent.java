package com.zskpaco.interception;

public class VisitorComponent {

    /**
     * 初始化
     */
    public static void replaceLine() {
        throw new RuntimeException(
                "do you have added 'interception-android' plugin to this module gradle?");
    }

}
