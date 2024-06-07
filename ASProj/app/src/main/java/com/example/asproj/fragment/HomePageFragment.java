package com.example.asproj.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.asproj.R;
import com.example.asproj.restful.HiCallback;
import com.example.asproj.restful.HiResponse;
import com.example.asproj.restful.api.AccountApi;
import com.example.asproj.restful.http.ApiFactory;
import com.example.common.HiBaseFragment;
import com.example.common.utils.AppGlobals;
import com.example.hilibrary.util.ActivityManager;
import com.example.hilibrary.util.HiExecutor;

import org.devio.as.proj.ability.HiAbility;
import org.devio.as.proj.ability.share.ShareBundle;
import org.json.JSONObject;

import kotlinx.coroutines.GlobalScope;


public class HomePageFragment extends HiBaseFragment {
    private boolean paused;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameTx = layoutView.findViewById(R.id.username);

        layoutView.findViewById(R.id.profile).setOnClickListener(
                v->navigation("/profile/detail")
        );

        layoutView.findViewById(R.id.vip).setOnClickListener(
                v -> navigation("/profile/vip")
        );
        layoutView.findViewById(R.id.authentication).setOnClickListener(
                v->navigation("/profile/authentication")
        );

        layoutView.findViewById(R.id.unkown).setOnClickListener(
                v->navigation("/profile/unkown")
        );
        layoutView.findViewById(R.id.pick).setOnClickListener(
                v->{
                    for(int priority = 0; priority < 10; priority++){
                        int finalPriory = priority;
                        HiExecutor.INSTANCE.execute(priority,()->{
                            try{
                                Thread.sleep(1000 - finalPriory * 100);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        });
                    }
                }
        );
        layoutView.findViewById(R.id.pick_city).setOnClickListener(
                v->{
                    if(paused){
                        HiExecutor.INSTANCE.resume();
                    }else{
                        HiExecutor.INSTANCE.pause();
                    }
                    paused = !paused;
                }
        );

        layoutView.findViewById(R.id.openScan).setOnClickListener(v->{
            HiAbility.INSTANCE.openScanActivity(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            });
        });

        layoutView.findViewById(R.id.share_QQ).setOnClickListener(v->{
            ShareBundle shareBundle = new ShareBundle();
            shareBundle.setTitle("测试分享title");
            shareBundle.setSummary("测试分享summary");
            shareBundle.setAppName("app_ability");
            shareBundle.setTargetUrl("https://www.baidu.com");
            shareBundle.setThumbUrl("https://img1.baidu.com/it/u=4145090583,1653500200&fm=253&fmt=auto&app=138&f=PNG?w=243&h=243");
//            Activity topActivity = ActivityManager.getInstance().getTopActivity(true);
            HiAbility.INSTANCE.openShareDialog(getActivity(),shareBundle);
        });
        layoutView.findViewById(R.id.mainThread).setOnClickListener(
//            v ->HiExecutor.INSTANCE.execute(new HiExecutor.Callable<String>() {
//                @Override
//                public void onCompleted(@Nullable String s) {
//                    Log.e("MainActivity","onCompleted-当前线程：" + Thread.currentThread().getName());
//                    Log.e("MainActivity","onCompleted-任务结果：" + s);
//                }
//
//                @Nullable
//                @Override
//                public String onBackground() {
//                    Log.e("MainActivity","onBackground-当前线程：" + Thread.currentThread().getName());
//                    return "我是异步任务的结果";
//                }
//            })
                v->{

                }
        );
    }

    private void getCity() {
    }

    void navigation(String path){
        ARouter.getInstance().build(path).navigation();
    }

      void openGallery() {
        var intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivity(intent);
    }
}
