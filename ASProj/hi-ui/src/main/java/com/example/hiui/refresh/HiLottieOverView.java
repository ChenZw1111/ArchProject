package com.example.hiui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hiui.R;

public class HiLottieOverView extends HiOverView{
    public HiLottieOverView(@NonNull Context context) {
        super(context);
    }

    public HiLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LottieAnimationView pullAnimationView;
    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.lottie_overview,this,true);
        pullAnimationView = findViewById(R.id.pull_animation);
        pullAnimationView.setAnimation("loading_wave.json");
    }

    @Override
    protected void onScroll(int scrollY, int mPullRefreshHeight) {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    public void onOver() {

    }

    @Override
    public void onRefresh() {
        pullAnimationView.setSpeed(2);
        pullAnimationView.playAnimation();
    }

    @Override
    public void onFinish() {
        pullAnimationView.setProgress(0f);
        pullAnimationView.cancelAnimation();
    }
}
