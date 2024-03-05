package com.example.hiui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hiui.tab.bottom.HiTabBottomInfo;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public interface IHiTabLayout<Tab extends ViewGroup, D> {
    Tab findTab(@NonNull D data);

    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    void defaultSelected(@NonNull D defaultInfo);

    void inflateInfo(@NonNull List<D> infoList);


    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index,  D prevInfo,  D nextInfo);
    }
}
