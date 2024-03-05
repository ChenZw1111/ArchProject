package com.example.asproj.logic;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.example.asproj.R;
import com.example.asproj.fragment.CategoryFragment;
import com.example.asproj.fragment.FavoriteFragment;
import com.example.asproj.fragment.HomeFragment;
import com.example.asproj.fragment.HomePageFragment;
import com.example.asproj.fragment.ProfileFragment;
import com.example.asproj.fragment.RecommendFragment;
import com.example.hiui.tab.HiFragmentTabView;
import com.example.hiui.tab.HiTabViewAdapter;
import com.example.hiui.tab.bottom.HiTabBottomInfo;
import com.example.hiui.tab.bottom.HiTabBottomLayout;
import com.example.hiui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {

    private final static String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";
    private HiFragmentTabView fragmentTabView;
    private HiTabBottomLayout hiTabBottomLayout;
    private List<HiTabBottomInfo<?>> infoList;
    private final ActivityProvider activityProvider;

    //当前停留的Fragment index
    private int currentItemIndex;
    public MainActivityLogic(ActivityProvider activityProvider,@NonNull Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        if(savedInstanceState !=null){
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
            Log.e("currentItemIndex2","currentItemIndex: "+currentItemIndex);
        }
        initTabBottom();
    }

    private void initTabBottom() {
        hiTabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout
        );
        hiTabBottomLayout.setTabAlpha(0.85f);
        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(com.example.common.R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(com.example.common.R.color.tabBottomTintColor);

        HiTabBottomInfo<Integer> homeInfo = new HiTabBottomInfo<Integer>(
                "首页",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        );
        homeInfo.fragment = HomePageFragment.class;

        HiTabBottomInfo<Integer> FavoriteInfo = new HiTabBottomInfo<>(
                "收藏",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null,
                defaultColor,
                tintColor
        );
        FavoriteInfo.fragment = FavoriteFragment.class;


        HiTabBottomInfo<Integer> CategoryInfo = new HiTabBottomInfo<>(
                "分类",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        );
        CategoryInfo.fragment = CategoryFragment.class;

        HiTabBottomInfo<Integer> RecommendInfo = new HiTabBottomInfo<>(
                "推荐",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null,
                defaultColor,
                tintColor
        );
        RecommendInfo.fragment = RecommendFragment.class;

        HiTabBottomInfo<Integer> ProfileInfo = new HiTabBottomInfo<>(
                "我的",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null,
                defaultColor,
                tintColor
        );
        ProfileInfo.fragment = ProfileFragment.class;
        infoList.add(homeInfo);
        infoList.add(CategoryInfo);
        infoList.add(RecommendInfo);
        infoList.add(ProfileInfo);
        infoList.add(FavoriteInfo);
        hiTabBottomLayout.inflateInfo(infoList);
        initFragmentTabView();
        hiTabBottomLayout.addTabSelectedChangeListener(new IHiTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelectedChange(int index, @NonNull Object prevInfo, @NonNull Object nextInfo) {
                fragmentTabView.setCurrentPosition(index);
                MainActivityLogic.this.currentItemIndex  = index;
                Log.e("currentItemIndex3","currentItemIndex: "+currentItemIndex);
            }
        });
        hiTabBottomLayout.defaultSelected(infoList.get(currentItemIndex));
        Log.e("currentItemIndex4","currentItemIndex: "+currentItemIndex);
    }

    private void initFragmentTabView(){
        HiTabViewAdapter tabViewAdapter = new HiTabViewAdapter(infoList, activityProvider.getSupportFragmentManager());
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(tabViewAdapter);
    }

    public void onSaveInstanceState(@NonNull Bundle outState){
        outState.putInt(SAVED_CURRENT_ID,currentItemIndex);
        Log.e("currentItemIndex1","currentItemIndex: "+currentItemIndex);
    }

    public HiFragmentTabView getFragmentTabView(){
        return fragmentTabView;
    }

    public HiTabBottomLayout getHiTabBottomLayout(){
        return hiTabBottomLayout;
    }
    public interface ActivityProvider {
        <T extends View> T findViewById(int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int redId);
    }
}
