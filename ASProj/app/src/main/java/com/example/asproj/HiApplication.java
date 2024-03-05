package com.example.asproj;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.asproj.route.BizInterceptor;
import com.example.common.HiBaseApplication;
import com.example.hilibrary.log.HiLog;
import com.example.hilibrary.log.HiLogConfig;
import com.example.hilibrary.log.HiLogManager;
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
        HiAbility.INSTANCE.init(this,"Umeng",null);
    }
}
