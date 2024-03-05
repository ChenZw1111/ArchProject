package com.example.common

import android.app.Application
import com.example.common.flutter.HiFlutterCacheManager
import com.example.common.utils.AppGlobals

open class HiBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiFlutterCacheManager.instance?.preLoad(this)
    }
}