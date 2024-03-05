package com.example.asproj.restful

interface HiCallback<T>{
    fun onSuccess(response: HiResponse<T>)
    fun onFailed(throwable:Throwable){}
}