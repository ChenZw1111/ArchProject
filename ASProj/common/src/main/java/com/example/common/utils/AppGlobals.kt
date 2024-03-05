package com.example.common.utils

import android.app.Application
import java.lang.Exception

object AppGlobals {
    private var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application =
                    Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                        .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return application
    }
}