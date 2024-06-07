package com.example.hiui.refresh

interface HiRefresh {
    /**
     * 刷新时是否禁止滚动
     * @param disableRefreshScroll
     */
    fun setDisableRefreshScroll(disableRefreshScroll: Boolean)

    /**
     * 刷新完成
     */
    fun refreshFinished()
    fun setRefreshOverView(hiOverView: HiOverView?)

    /**
     * 设置下拉刷新的监听器
     * @param hiRefreshListener
     */
    fun setRefreshListener(hiRefreshListener: HiRefreshListener?)
    interface HiRefreshListener {
        fun onRefresh()
        fun enableRefresh(): Boolean
    }
}