package com.example.hiui.activity

import android.app.Application
import com.example.hilibrary.log.HiConsolePrinter
import com.example.hilibrary.log.HiLogConfig
import com.example.hilibrary.log.HiLogManager
import com.google.gson.Gson

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(
            object : HiLogConfig() {
                override fun injectJsonParser(): JsonParser? {
                    return JsonParser { src: Any? -> Gson().toJson(src) }
                }

                override fun getGlobalTag(): String {
                    return "MApplication"
                }

                override fun enable(): Boolean {
                    return true
                }

                override fun includeThread(): Boolean {
                    return false
                }

                override fun stackTraceDepth(): Int {
                    return 0
                }
            },
            HiConsolePrinter(),
        )
    }
}