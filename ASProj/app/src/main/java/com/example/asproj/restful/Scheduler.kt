package com.example.asproj.restful

import android.annotation.SuppressLint
import com.example.asproj.cache.HiStorage
import com.example.asproj.restful.annotation.CacheStrategy
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.HiInterceptor
import com.example.hilibrary.util.MainHandler

class Scheduler(
    private val callFactory: HiCall.Factory,
    private val interceptors: MutableList<HiInterceptor>
) {
    fun newCall(request: HiRequest): HiCall<*> {
        val newCall: HiCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }

    internal inner class ProxyCall<T : Any?>(
        private val delegate: HiCall<T>,
        private val request: HiRequest
    ) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            val response = delegate.execute()
            dispatchInterceptor(request, response)

            return response
        }

        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(request, null)
            if(request.cacheStrategy == CacheStrategy.CACHE_FIRST){
                //线程中执行 HiExecutor
                val cacheResponse = readCache(request)
                if(cacheResponse.data != null){
                    MainHandler.sendAtFrontOfQueue(runnable = {
                        callback.onSuccess(cacheResponse)
                    })
                }
            }
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    saveCacheIfNeed(response)
                    if (callback != null) callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    if (callback != null) callback.onFailed(throwable)
                }
            })
        }

        private fun saveCacheIfNeed(response: HiResponse<T>) {
            if(request.cacheStrategy == CacheStrategy.CACHE_FIRST
                || request.cacheStrategy == CacheStrategy.NET_CACHE){
                if(response.data != null){
                    HiStorage.saveCache(request.getCacheKey(),response.data)
                }
            }

        }

        private fun readCache(request: HiRequest): HiResponse<T> {
            val cacheKey = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey)
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存成功"
            return cacheResponse
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun dispatchInterceptor(
        request: HiRequest,
        response: HiResponse<*>?
    ) {
        if (interceptors.size > 0)
        InterceptorChain(request, response).dispatch()
    }

    internal inner class InterceptorChain(
        private val request: HiRequest,
        private val response: HiResponse<*>?
    ) : HiInterceptor.Chain {
        var callIndex: Int = 0

        override val isRequestPeriod: Boolean
            get() = response == null

        override fun request(): HiRequest {
            return request
        }

        override fun response(): HiResponse<*>? {
            return response
        }

        fun dispatch() {
            val interceptor = interceptors[callIndex]
            val intercept = interceptor.intercept(this)
            callIndex++
            if (!intercept && callIndex < interceptors.size) {
                dispatch()
            }
        }
    }
}