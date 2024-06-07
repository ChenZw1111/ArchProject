package com.example.hiui.hiitem

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
abstract class HiDataItem<DATA,VH:RecyclerView.ViewHolder>(data:DATA?=null) {
    public lateinit var mAdapter: HiAdapter
    var mData: DATA?=null
    init {
        mData = data
    }

    abstract fun onBindData(holder:VH,position:Int)

    /**
     * 返回item的布局资源id
     */
    open fun getItemLayoutRes():Int{
        return -1
    }

    /**
     * 返回该item的视图view
     */
    open fun getItemView(parent:ViewGroup):View?{
        return null
    }

    /**
     * 刷新列表
     */
    fun refreshItem(){
        mAdapter.refreshItem(this)
    }

    /**
     * 从列表上移除
     */
    fun removeItem(){
        mAdapter.removeItem(this as HiDataItem<*,RecyclerView.ViewHolder>)
    }

    /**
     * 该Item在列表上占据几列
     */
    open fun getSpanSize():Int{
        return 0
    }

    fun setAdapter(adapter:HiAdapter){
        this.mAdapter = adapter
    }

    /**
     * item被滑进屏幕
     */
    open fun onViewAttachedToWindow(holder:VH){

    }

    open fun onViewDetachedFromWindow(holder: VH){

    }

    open fun onCreateViewHolder(parent:ViewGroup):VH?{
        return null
    }
}