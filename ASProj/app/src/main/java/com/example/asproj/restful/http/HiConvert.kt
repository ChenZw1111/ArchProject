package com.example.asproj.restful.http

import com.example.asproj.restful.HiResponse
import java.lang.reflect.Type

interface HiConvert {
    fun<T>convert(rawData:String,dataType:Type): HiResponse<T>
}