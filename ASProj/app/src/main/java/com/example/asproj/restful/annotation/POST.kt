package com.example.asproj.restful.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String, val formPost: Boolean = true)