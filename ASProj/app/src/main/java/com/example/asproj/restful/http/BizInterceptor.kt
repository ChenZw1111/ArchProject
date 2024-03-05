package com.example.asproj.restful.http

import com.example.asproj.restful.annotation.HiInterceptor
import com.example.hilibrary.log.HiLog

class BizInterceptor :HiInterceptor{
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if(chain.isRequestPeriod){
            val request = chain.request()
            request.addHeader("auth-token","MjAyMC0wNi0yMyAwMzoyNTowMQ==")
        }else if(chain.response() != null){
//            HiLog.dt("BizInterceptor",chain.request().endPointUrl())
//            HiLog.dt("BizInterceptor",chain.response()!!.rawData)
        }
        return false
    }
}