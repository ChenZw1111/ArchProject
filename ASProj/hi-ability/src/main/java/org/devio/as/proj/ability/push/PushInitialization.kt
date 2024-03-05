package org.devio.`as`.proj.ability.push

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import anet.channel.util.Utils.context
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.api.UPushRegisterCallback
import com.umeng.message.entity.UMessage
import org.devio.`as`.proj.ability.BuildConfig
import org.json.JSONObject


object PushInitialization {
    const val TAG = "PushInitialization"
    fun init(application: Application, channel: String,iPushMessageHandler: IPushMessageHandler?=null) {
        initUmengPushSdk(application, channel,iPushMessageHandler)
//        initOEMPushSdk()
    }

    private fun initOEMPushSdk() {

    }

    private fun initUmengPushSdk(application: Application, channel: String,iPushMessageHandler: IPushMessageHandler?=null) {
        UMConfigure.init(
            application,
            BuildConfig.UMENG_API_KEY,
            channel,
            UMConfigure.DEVICE_TYPE_PHONE,
            BuildConfig.UMENG_MESSAGE_SECRET
        )

        val mPushAgent = PushAgent.getInstance(context)
        mPushAgent.register(object : UPushRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                iPushMessageHandler?.onRegisterSuccess(deviceToken)
                Log.i(TAG, "注册成功 deviceToken:$deviceToken")
            }

            override fun onFailure(errCode: String, errDesc: String) {
                Log.e(TAG, "注册失败 code:$errCode, desc:$errDesc")
                iPushMessageHandler?.onRegisterFailed(errCode,errDesc)
            }
        })

        mPushAgent.messageHandler = object : UmengMessageHandler() {
            override fun dealWithNotificationMessage(p0: Context?, p1: UMessage?) {
                if(iPushMessageHandler !=null){
                    iPushMessageHandler.dealWithNotificationMessage(p0, JSONObject())
                }else{
                    super.dealWithNotificationMessage(p0, p1)
                }
            }

            override fun dealWithCustomMessage(p0: Context?, p1: UMessage?) {
                if(iPushMessageHandler !=null){
                    iPushMessageHandler.dealWithCustomMessage(p0, JSONObject())
                }else{
                    super.dealWithCustomMessage(p0, p1)
                }
            }

        }

        mPushAgent.notificationClickHandler = object : UmengNotificationClickHandler(){
            override fun dealWithCustomAction(p0: Context?, p1: UMessage?) {
                if(iPushMessageHandler !=null){
                    iPushMessageHandler.dealWithCustomAction(p0, JSONObject())
                }else{
                    super.dealWithCustomAction(p0, p1)
                }
            }
        }

        application.registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks() {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPreCreated(activity, savedInstanceState)
                PushAgent.getInstance(activity.application).onAppStart()
            }
        })
    }

}