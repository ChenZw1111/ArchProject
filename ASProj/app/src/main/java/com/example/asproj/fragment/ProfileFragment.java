package com.example.asproj.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.asproj.R;
import com.example.asproj.biz.account.AccountManager;
import com.example.asproj.model.UserProfile;
import com.example.asproj.restful.HiCallback;
import com.example.asproj.restful.HiResponse;
import com.example.asproj.restful.api.AccountApi;
import com.example.asproj.restful.http.ApiFactory;
import com.example.common.HiBaseFragment;
import com.example.common.utils.SPUtil;
import com.example.hilibrary.util.MainHandler;
import com.example.hiui.DialogManager;
import com.example.hiui.DialogView;

public class ProfileFragment extends HiBaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }
    TextView usernameTx;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameTx = layoutView.findViewById(R.id.username);

        usernameTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccountManager.INSTANCE.isLogin()){
                    DialogView dialogView = DialogManager.getInstance().initView(getContext(), R.layout.dialog_logout, Gravity.CENTER);
                    dialogView.findViewById(R.id.anmv_determine).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiFactory.INSTANCE.create(AccountApi.class).logout().enqueue(new HiCallback<String>() {
                                @Override
                                public void onFailed(@NonNull Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(@NonNull HiResponse<String> response) {
                                    AccountManager.INSTANCE.logout();
                                    usernameTx.setText("未登录");
                                }
                            });
                            DialogManager.getInstance().hide(dialogView);
                        }
                    });
                    DialogManager.getInstance().show(dialogView);
                }else{
                    AccountManager.INSTANCE.login(getContext(), (Observer<Boolean>) success -> {
                        queryUserData();
                    });
                }
            }
        });
//        queryUserData();
    }

    private void queryUserData(){
        if(AccountManager.INSTANCE.isLogin()){
            AccountManager.INSTANCE.getUserProfile(this, profile -> {
                if(profile !=null){
                    updateUI(profile);
                }else{
//                    Toast.makeText(getContext(),"用户信息获取失败",Toast.LENGTH_SHORT).show();
                }
            },true);

        }else{

        AccountManager.INSTANCE.getUserProfile(this, profile -> {
            if(profile !=null){
                updateUI(profile);
            }else{
//                Toast.makeText(getContext(),"用户信息获取失败",Toast.LENGTH_SHORT).show();
            }
        },false);
        }
    }
   private void updateUI(UserProfile profile){
        usernameTx.setText(profile.getUserName());
   }
}
