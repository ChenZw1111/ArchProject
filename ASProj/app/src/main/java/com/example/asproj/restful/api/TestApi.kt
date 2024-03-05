package com.example.asproj.restful.api

import com.example.asproj.restful.annotation.Filed
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.POST

interface TestApi {
    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>
}