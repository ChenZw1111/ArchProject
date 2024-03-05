package com.example.asproj.restful.annotation

import com.example.asproj.restful.HiCallback
import com.example.asproj.restful.HiRequest
import com.example.asproj.restful.HiResponse
import java.io.IOException

interface HiCall<T> {
    @Throws(IOException::class)
    fun execute(): HiResponse<T>

    fun enqueue(callback: HiCallback<T>)

    interface Factory{
        fun newCall(request: HiRequest): HiCall<*>
    }
}