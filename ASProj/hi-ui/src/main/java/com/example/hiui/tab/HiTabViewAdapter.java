package com.example.hiui.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hiui.tab.bottom.HiTabBottomInfo;

import java.util.List;

public class HiTabViewAdapter {
    private List<HiTabBottomInfo<?>> mInfoList;
    private Fragment mCurFragment;
    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(List<HiTabBottomInfo<?>> mInfoList, FragmentManager mFragmentManager) {
        this.mInfoList = mInfoList;
        this.mFragmentManager = mFragmentManager;
    }

    /**
     * 实例化以及显示指定位置的fragment
     */
    public void instantiateItem(View container, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mCurFragment != null) {
            //隐藏当前Fragment
            fragmentTransaction.hide(mCurFragment);
        }
        String name = container.getId() + ":" + position;
        //通过Tag查找Fragment
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            fragmentTransaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                //添加fragment时 添加fragment的tag
                fragmentTransaction.add(container.getId(), fragment, name);
            }
        }
        mCurFragment = fragment;
        fragmentTransaction.commit();
    }

    public Fragment getItem(int position) {
        try {
            return mInfoList.get(position).fragment.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public Fragment getCurFragment() {
        return mCurFragment;
    }

    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }
}
