package com.example.asproj.restful.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl(val value:String)