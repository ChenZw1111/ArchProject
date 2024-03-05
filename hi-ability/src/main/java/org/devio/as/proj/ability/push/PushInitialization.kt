package org.devio.`as`.proj.ability.push

import android.app.Application
import com.umeng.commonsdk.UMConfigure

object PushInitialization {
    fun init(application: Application){
        initUmengPushSdk(application)
        initOEMPushSdk()
    }

    private fun initOEMPushSdk() {

    }

    private fun initUmengPushSdk(application: Application) {
        UMConfigure.init(
            application,
            BuildConfig.UM
        )
    }
}