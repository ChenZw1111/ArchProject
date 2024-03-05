package com.example.hiui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hiui.R;
import com.example.hiui.tab.bottom.HiTabBottomInfo;
import com.example.hiui.tab.common.IHiTab;

public class HiTabTop extends RelativeLayout implements IHiTab<HiTabTopInfo<?>> {
    private HiTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabNameView;
    private View indicator;

    public HiTabTop(Context context){
        this(context,null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top,this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false,true);
    }

    private void inflateInfo(boolean selected,boolean init){
        if(tabInfo.tabType == HiTabTopInfo.TabType.TXT){
            if (init) {
                tabNameView.setVisibility(VISIBLE);
                tabImageView.setVisibility(GONE);
                if(!TextUtils.isEmpty(tabInfo.name)){
                    tabNameView .setText(tabInfo.name);
                }
            }

            if(selected){
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            }else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        }else if(tabInfo.tabType == HiTabTopInfo.TabType.BITMAP){
            if (init) {
                tabNameView.setVisibility(GONE);
                tabImageView.setVisibility(VISIBLE);
            }
            if(selected){
                indicator.setVisibility(VISIBLE);
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            }else{
                indicator.setVisibility(GONE);
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    public HiTabTopInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(View.GONE);
    }



    private int getTextColor(Object color){
        if(color instanceof  String){
            return Color.parseColor((String)color);
        }
        return (int) color;
    }

    @Override
    public void onTabSelectedChange(int index, @NonNull HiTabTopInfo prevInfo, @NonNull HiTabTopInfo nextInfo) {
            if(prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo){
                return;
            }
            inflateInfo(prevInfo != tabInfo,false);
    }
}
