package com.example.asproj;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.asproj.route.BizInterceptor;
import com.example.common.HiBaseApplication;
import com.example.hilibrary.log.HiConsolePrinter;
import com.example.hilibrary.log.HiLog;
import com.example.hilibrary.log.HiLogConfig;
import com.example.hilibrary.log.HiLogManager;
import com.example.hilibrary.log.HiLogPrinter;
import com.google.gson.Gson;

import org.devio.as.proj.ability.HiAbility;

public class HiApplication extends HiBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        HiLogManager.init(new HiLogConfig() {
                              @Override
                              public JsonParser injectJsonParser() {
                                  return new JsonParser() {
                                      @Override
                                      public String toJson(Object src) {
                                          return  new Gson().toJson(src);
                                      }
                                  };
                              }

                              @Override
                              public String getGlobalTag() {
                                  return "Application";
                              }

                              @Override
                              public boolean enable() {
                                  return true;
                              }

                              @Override
                              public boolean includeThread() {
                                  return false;
                              }

                              @Override
                              public int stackTraceDepth() {
                                  return 0;
                              }

                              @Override
                              public HiLogPrinter[] printers() {
                                  return super.printers();
                              }
                          });
                HiAbility.INSTANCE.init(this, "Umeng", null);
    }
}
