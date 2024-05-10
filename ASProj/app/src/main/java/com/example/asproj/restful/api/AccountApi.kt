package com.example.asproj.restful.api

import com.example.asproj.model.UserProfile
import com.example.asproj.restful.annotation.Filed
import com.example.asproj.restful.annotation.GET
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.POST

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ):HiCall<String>

    @POST("user/registration")
    fun register(@Filed("userName") userName: String, @Filed("password") password: String,@Filed("phoneNum") phoneNum:String):HiCall<String>

   @POST("user/logout")
   fun logout():HiCall<String>

   @GET("user/profile")
   fun getProfile() :HiCall<UserProfile>
}