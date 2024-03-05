package com.example.asproj.Demo;

public interface HomeContract {
    interface View extends BaseView{
//        void onGetUserInfoResult(User user,String errorMsg);
    }

    abstract class Presenter extends BasePresenter<View>{
        abstract void getUserInfo();
    }
}
