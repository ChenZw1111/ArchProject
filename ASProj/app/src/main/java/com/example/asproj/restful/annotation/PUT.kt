package com.example.asproj.restful.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation  class PUT(val value:String,val formPost:Boolean = false)