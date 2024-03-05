package com.example.hilibrary.util

import android.os.Handler
import android.os.Looper
import android.os.Message

object MainHandler {
    private val mainHandler =
        Handler(Looper.getMainLooper())

    fun post(runnable:Runnable){
        mainHandler.post(runnable)
    }

    fun postDelay(delayMills:Long,runnable: Runnable){
        mainHandler.postDelayed(runnable,delayMills )
    }

    /**
     *插入到消息队列的最前面，从而使得消息尽快得到执行，在处理紧急任务的时候可以使用这个api
     */
    fun sendAtFrontOfQueue(runnable: Runnable){
        val message = Message.obtain(mainHandler,runnable)
        mainHandler.sendMessageAtFrontOfQueue(message)
    }

    fun remove(runnable: Runnable){
        mainHandler.removeCallbacks(runnable)
    }
}