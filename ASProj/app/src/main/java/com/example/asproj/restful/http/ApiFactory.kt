package com.example.asproj.restful.http

import android.util.Log
import com.example.asproj.restful.HiRestful

object ApiFactory {
//    https://api.xygeng.cn/one
    private val baseUrl = "http://192.168.11.176:8001/"
    private val hiRestful:HiRestful = HiRestful(baseUrl,RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())
    }

    fun<T> create(service:Class<T>):T{
        return hiRestful.create(service)

    }
}