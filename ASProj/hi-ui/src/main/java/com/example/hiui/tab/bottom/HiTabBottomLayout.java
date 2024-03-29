package com.example.hiui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hilibrary.util.HiDisplayUtil;
import com.example.hilibrary.util.HiViewUtil;
import com.example.hiui.R;
import com.example.hiui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 1.透明度和底部，列表可渲染高度
 * 2.中间高度错过，凸起布局
 */
public class HiTabBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottomG, HiTabBottomInfo<?>> {
    private List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private HiTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private static float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";
    private List<HiTabBottomInfo<?>> infoList;

    public HiTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public HiTabBottomG findTab(@NonNull HiTabBottomInfo<?> data) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabBottomG) {
                HiTabBottomG tab = (HiTabBottomG) child;
                if (tab.getTabInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    public void setTabAlpha(float Alpha) {
        this.bottomAlpha = Alpha;
    }

    public static void setTabBottomHeight(float tabBottomHeight) {
        HiTabBottomLayout.tabBottomHeight = tabBottomHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottomG) {
                iterator.remove();
            }
        }
        int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
        FrameLayout ll = new FrameLayout(getContext());
        int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        ll.setTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < infoList.size(); i++) {
            HiTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            HiTabBottomG hiTabBottom = new HiTabBottomG(getContext());
            tabSelectedListeners.add(hiTabBottom);
            hiTabBottom.setHiTabInfo(info);
            ll.addView(hiTabBottom, params);
            hiTabBottom.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSelected(info);
                        }
                    }
            );
        }
        LayoutParams flParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, flParams);

        fixContentView();
    }



    private void onSelected(@NonNull HiTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> listener : tabSelectedListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                HiDisplayUtil.dp2px(bottomLineHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight,
                getResources());
        addView(bottomLine, params);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void fixContentView(){
        if(!(getChildAt(0) instanceof ViewGroup)){
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = HiViewUtil.findTypeView(rootView, RecyclerView.class);
        if(targetView == null){
            targetView = HiViewUtil.findTypeView(rootView, ScrollView.class);
        }
//        if(targetView == null){
//            targetView == HiViewUtil.findTypeView(rootView, AbsListView.class);
//        }
        if(targetView != null){
            targetView.setPadding(0,0,0,HiDisplayUtil.dp2px(tabBottomHeight,getResources()));
            targetView.setClipToPadding(false);
        }
    }
}
