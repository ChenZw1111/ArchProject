package com.example.hiui.refresh;

public interface HiRefresh {
    /**
     * 刷新时是否禁止滚动
     * @param disableRefreshScroll
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    void setRefreshOverView(HiOverView hiOverView);
    /**
     * 设置下拉刷新的监听器
     * @param hiRefreshListener
     */
    void setRefreshListener(HiRefresh.HiRefreshListener hiRefreshListener);

    interface HiRefreshListener{
        void onRefresh();

        boolean enableRefresh();
    }
}
