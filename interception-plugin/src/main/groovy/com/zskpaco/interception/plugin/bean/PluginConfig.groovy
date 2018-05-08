package com.zskpaco.interception.plugin.bean

class PluginConfig {

    boolean debug = false

    String activityInit = "setContentView"

    void activityInit(String activityInit) {
        this.activityInit = activityInit
    }

}
