package com.example.common.flutter

import android.content.Context
import android.os.Looper
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterMain

class HiFlutterCacheManager private constructor(){

    /**
     * 预加载Flutter
     */
    fun preLoad(context: Context){
        Looper.myQueue().addIdleHandler {
            initFlutterEngine(context, MODULE_NAME_FAVORITE)
            initFlutterEngine(context, MODULE_NAME_RECOMMEND)
            false
        }
    }

    /**
     * 获取预加载的Flutter
     */
    fun getCacheFlutterEngine(moduleName: String,context: Context?):FlutterEngine{
        var engine = FlutterEngineCache.getInstance()[moduleName]
        if(engine == null && context !=null){
            engine = initFlutterEngine(context,moduleName)
        }
        return engine!!
    }
    /**
     * 初始化Flutter
     */
    private fun initFlutterEngine(context: Context,moduleName:String):FlutterEngine{
        val flutterEngine = FlutterEngine(context)
        HiFlutterBridge.init(flutterEngine)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint(
                FlutterMain.findAppBundlePath(),
                moduleName
            )
        )
        FlutterEngineCache.getInstance().put(moduleName,flutterEngine)
        return flutterEngine
    }
    companion object{
        const val MODULE_NAME_FAVORITE = "main"
        const val MODULE_NAME_RECOMMEND = "recommend"
        @JvmStatic
        @get:Synchronized
        var instance:HiFlutterCacheManager?= null
            get(){
                if(field == null){
                    field = HiFlutterCacheManager()
                }
                return field
            }
    }
}