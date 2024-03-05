package com.example.asproj.Demo;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {
    protected  P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Type superclass = this.getClass().getGenericSuperclass();
        if(superclass instanceof ParameterizedType){
            Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
            if(arguments != null && arguments[0] instanceof BasePresenter){
                try {
                    mPresenter = (P) arguments[0].getClass().newInstance();
                    mPresenter.attach(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detach();
        }
    }

    @Override
    public boolean isAlive() {
        return !isDestroyed() && !isFinishing();
    }
}
