package com.example.hiui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

import com.example.hiui.tab.bottom.HiTabBottomInfo;

public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D>{
    void setHiTabInfo(@NonNull D data);

    /**
     * 动态修改某个item的大小
     */
    void resetHeight(@Px int height);

}
