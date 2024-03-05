package com.example.asproj.restful.http

import com.alibaba.android.arouter.launcher.ARouter
import com.example.asproj.restful.HiResponse
import com.example.asproj.restful.annotation.HiInterceptor

class HttpStatusInterceptor:HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response()
        if(!chain.isRequestPeriod && response != null){
            val code = response.code
            when(code){
                HiResponse.RC_NEED_LOGIN ->{
                    ARouter.getInstance().build("/account/login").navigation()
                }

                HiResponse.RC_USER_FORBID ->{
                    var helpurl:String? = null
                    if(response.errorData != null){
                        helpurl = response.errorData!!.get("helpurl")
                    }
                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title","非法访问")
                        .withString("degrade_desc",response.msg)
                        .withString("degrade_action",helpurl)
                        .navigation()
                }
            }
        }
        return false
    }
}