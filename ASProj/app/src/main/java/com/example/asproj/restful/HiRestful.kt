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
    //根据传进来的接口class对象，返回一个接口的代理对象
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service), object :InvocationHandler{
                //bugfix 需要考虑空参数，args有可能为空
                override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any {

                    var methodParser = methodService[method]
                    if (methodParser == null) {
                        methodParser = MethodParser.parse(baseUrl, method)
                        methodService.put(method, methodParser)
                    }

                    //bugfix：此处考虑到methodParser复用，多次调用参数有可能不同，
                    val newRequest = methodParser.newRequest(method,args)

//            callFactory.newCall(newRequest)
                    return scheduler.newCall(newRequest)
                }
            }) as T
    }
}