package com.example.common.flutter

import com.alibaba.android.arouter.launcher.ARouter
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class HiFlutterBridge:IHiBridge<Any?,MethodChannel.Result>,MethodChannel.MethodCallHandler {
    private var methodChannels = mutableListOf<MethodChannel>()
    companion object{
        @JvmStatic
        var instance:HiFlutterBridge? = null

        @JvmStatic
        fun init(flutterEngine: FlutterEngine):HiFlutterBridge?{
            val methodChannel = MethodChannel(flutterEngine.dartExecutor,
            "HiFlutterBridge")
            if(instance == null){
                HiFlutterBridge().also { instance = it }
            }
            methodChannel.setMethodCallHandler(instance)
            instance!!.apply {
                methodChannels.add(methodChannel)
            }
            return instance
        }
    }

    fun fire(method:String,arguments:Any?){
        methodChannels.forEach{
            it.invokeMethod(method,arguments)
        }
    }

    fun fire(method:String,arguments: Any?,callBack: MethodChannel.Result){
        methodChannels.forEach {
            it.invokeMethod(method,arguments,callBack)
        }
    }
    override fun onBack(p: Any?) {
    }

    override fun goToNative(p: Any?) {
        if(p is Map<*,*>){
            val action = p["action"]
            if(action == "goToLogin"){
                val username = p["username"]
                ARouter.getInstance().build("/account/login").withString(
                    "username",
                    username as String?
                ).navigation()
            }
        }
    }

    override fun getHeaderParams(callBack: MethodChannel.Result) {
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method){
            "onBack" ->onBack(call.arguments)
            "getHeaderParams" -> getHeaderParams(result)
            "goToNative" -> goToNative(call.arguments)
            else -> result.notImplemented()
        }
    }
}