package com.example.asproj.restful

import android.annotation.SuppressLint
import android.util.Log
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.HiInterceptor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

class HiRestful constructor(val baseUrl: String, callFactory: HiCall.Factory) {
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private var scheduler: Scheduler
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    init {
        scheduler = Scheduler(callFactory, interceptors)
    }

    @SuppressLint("SuspiciousIndentation")
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            var methodParser = methodService.get(method)
            if (methodParser == null) {
                methodParser = MethodParser.parse(baseUrl, method, args)
                methodService[method] = methodParser
            }

            val request = methodParser.newRequest(method, args)
            scheduler.newCall(request)
        } as T
    }
}