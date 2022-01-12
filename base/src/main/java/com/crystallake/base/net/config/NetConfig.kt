package com.crystallake.base.net.config


object NetConfig {

    private val DEFULT = object : GlobalConfig {}

    private var mConfig: GlobalConfig = DEFULT

    fun install(config: GlobalConfig) {
        mConfig = config
    }

    fun getConfig() = mConfig
}