package com.example.asproj.Demo;

public class BasePresenter <IView extends BaseView>{
    private IView view;

    public void attach(IView view){
        this.view = view;
    }

    public void detach(){
        view = null;
    }
}
