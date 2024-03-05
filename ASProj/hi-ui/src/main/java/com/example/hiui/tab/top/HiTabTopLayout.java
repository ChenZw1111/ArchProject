package com.example.hiui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.hilibrary.util.HiDisplayUtil;
import com.example.hiui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop,HiTabTopInfo<?>> {
    private List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private HiTabTopInfo<?> selectedInfo;
    private List<HiTabTopInfo<?>> infoList;
    public HiTabTopLayout(Context context) {
        this(context,null);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if(child instanceof HiTabTop){
                HiTabTop tab = (HiTabTop) child;
                if(tab.getTabInfo() == info){
                    return tab;
                }
            }
        }
        return null;
    }


    @Override
    public void defaultSelected(@NonNull HiTabTopInfo defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if(infoList.isEmpty()){
            return;
        }
        this.infoList = infoList;
        LinearLayout linearLayout = getRootLayout(true);
        selectedInfo = null;
        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterator = tabSelectedListeners.iterator();
        while(iterator.hasNext()){
            if(iterator.next() instanceof HiTabTop){
                iterator.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            HiTabTopInfo<?> info = infoList.get(i);
            HiTabTop hiTabTop = new HiTabTop(getContext());
            tabSelectedListeners.add(hiTabTop);
            hiTabTop.setHiTabInfo(info);
            linearLayout.addView(hiTabTop);
            hiTabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
    }

    private void onSelected(HiTabTopInfo<?> info) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> listener : tabSelectedListeners){
            listener.onTabSelectedChange(infoList.indexOf(info),selectedInfo,info);
        }
        this.selectedInfo = info;
        autoScroll(info);
    }

    int tabWidth;
    /**
     * 自动滚动，实现点击位置能够自动滚动以展示前后两个
     * @param info
     */
    private void autoScroll(HiTabTopInfo<?> info) {
        HiTabTop tabTop = findTab(info);
        if(tabTop == null) return;
        int index = infoList.indexOf(info);
        int[] loc = new int[2];
        //获取点击的控件在屏幕的位置
        tabTop.getLocationInWindow(loc);
        int scrollWidth;
        if(tabWidth == 0){
            tabWidth = tabTop.getWidth();
        }
        Log.e("scrollLoc",loc[0]+"");
        //判断点击了屏幕左侧还是右侧
        if((loc[0] + tabWidth /2 ) > HiDisplayUtil.getDisplayWidthInPx(getContext()) /2){
            scrollWidth = rangeScrollWidth(index,2);//
        }else{
            scrollWidth = rangeScrollWidth(index,-2);//
        }
        Log.e("scrollWidth",scrollWidth+"");
        scrollTo(getScrollX() + scrollWidth,0);
    }

    /**
     * 获取可滚动的范围
     * @param index
     * @param range
     * @return
     */
   private int rangeScrollWidth(int index,int range){
        int scrollWidth = 0;
       for (int i = 0; i <= Math.abs(range); i++) {
           int next;
           if(range < 0){
               next = range + i + index;
           }else{
               next = range - i + index;
           }
           Log.e("scrollnext",next+"");
           if(next >= 0 && next < infoList.size()){
               if(range < 0){
                   scrollWidth -=scrollWidth(next,false);
               }else{
                   scrollWidth +=scrollWidth(next,true);
               }
           }
       }
       return scrollWidth;
   }

    /**
     *
     * @param index
     * @param toRight
     * @return
     */
    private int scrollWidth(int index,boolean toRight){
        HiTabTop target = findTab(infoList.get(index));
        if(target == null) return 0;
        Rect rect = new Rect();
        //视图是否可见
        target.getLocalVisibleRect(rect);
        Log.e("scrollRect","left: "+rect.left+"  right: "+rect.right+"   -tabwidth- "+ tabWidth);
        if(toRight){
            if(rect.right > tabWidth){
                return tabWidth;
            }else{
                return tabWidth - rect.right;
            }
        }else{
            if(rect.left <= -tabWidth){
                return tabWidth;
            }else if(rect.left > 0){
                return rect.left;
            }
            return 0;
        }
    }
    private LinearLayout getRootLayout(boolean clear){
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if(rootView == null){
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(rootView,layoutParams);
        }else if(clear){
            rootView.removeAllViews();
        }
        return rootView;
    }
}
