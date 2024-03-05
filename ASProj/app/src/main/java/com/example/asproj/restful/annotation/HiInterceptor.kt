package com.example.asproj.restful.annotation

import com.example.asproj.restful.HiRequest
import com.example.asproj.restful.HiResponse


interface HiInterceptor {
    fun intercept(chain:Chain):Boolean

    interface Chain{
        val isRequestPeriod:Boolean get() = false

        fun request():HiRequest

        fun response():HiResponse<*>?
    }
}