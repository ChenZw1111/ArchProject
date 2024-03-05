package com.example.asproj.restful.api

import com.example.asproj.restful.annotation.Filed
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("username") userName: String,
        @Filed("password") password: String
    ):HiCall<String>

    @POST("user/registration")
    fun register(@Filed("username") userName: String, @Filed("password") password: String):HiCall<String>
}