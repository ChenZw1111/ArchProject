package org.devio.`as`.proj.ability.push

import android.content.Context
import org.json.JSONObject

interface IPushMessageHandler {

    //自定义 处理 通知栏 点击行为
    fun dealWithCustomAction(p0: Context?, message: JSONObject?) {

    }

    // 自定义消息格式，弹窗
    fun dealWithCustomMessage(p0: Context?, message: JSONObject?) {


    }

    //自定义notification
    fun dealWithNotificationMessage(p0: Context?, message: JSONObject?) {


    }

    fun onRegisterSuccess(deviceToken: String) {


    }

    fun onRegisterFailed(errCode: String, errDesc: String) {

    }


}