package com.example.hiui.hiitem

import android.content.Context
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

class HiAdapter(context: Context) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext:Context
    private var mInflater:LayoutInflater
    private var heads = SparseArray<View>()
    private var foots = SparseArray<View>()
    private var BASE_ITEM_TYPE_HEAD = 100000
    private var BASE_ITEM_TYPE_FOOTER = 200000
    private var dataSets = ArrayList<HiDataItem<*,out RecyclerView.ViewHolder>>()
    private var typePosition = SparseIntArray()
    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }
    fun addHeadView(headView:View){
        if(heads.indexOfValue(headView) < 0){
            heads.put(BASE_ITEM_TYPE_HEAD++,headView)
            notifyItemInserted(heads.size() - 1)
        }
    }
    fun removeHeaderView(view: View){
        val indexOfValue = heads.indexOfValue(view)
        if(indexOfValue < 0)return
        heads.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }
    fun addFootView(footView:View){
        if(foots.indexOfValue(footView) < 0){
            foots.put(BASE_ITEM_TYPE_FOOTER++,footView)
            notifyItemInserted(itemCount)
        }
    }
    fun removeFooterView(view:View){
        val indexOfValue = foots.indexOfValue(view)
        if(indexOfValue < 0) return
        foots.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue + getHeadSize() + getOriginalItemCount())
    }

    fun getHeadSize():Int{
        return heads.size()
    }

    fun getFootSize():Int{
        return foots.size()
    }

    fun getOriginalItemCount():Int{
        return dataSets.size
    }
    fun addItem(index:Int, item:HiDataItem<*,out RecyclerView.ViewHolder>,notify:Boolean){
        if(index > 0){
            dataSets.add(index,item)
        }else{
            dataSets.add(item)
        }
        val notifyPos = if(index > 0) index else dataSets.size - 1
        if(notify){
            notifyItemChanged(notifyPos)
        }
        item.setAdapter(this)
    }

    fun addItem(list:List<HiDataItem<*,out RecyclerView.ViewHolder>>,notify: Boolean){
        val start = dataSets.size
        for (item in list){
            dataSets.add(item)
            item.setAdapter(this)
        }

        if(notify){
            notifyItemRangeChanged(start,list.size)
        }
    }

    fun removeItem(index:Int):HiDataItem<*,out RecyclerView.ViewHolder>?{
        if(index > 0 && index < dataSets.size){
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return remove
        }else{
            return null
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(isHeadPosition(position)){
            return heads.indexOfKey(position)
        }
        if(isFooterPosition(position)){
            val footerPosition = getHeadSize() + getOriginalItemCount() + position
            return foots.indexOfKey(footerPosition)
        }
        val itemPosition = position - getHeadSize()
        val item = dataSets[itemPosition]
        val type = item.javaClass.hashCode()
        typePosition.put(type,position)
        return type
    }
    private fun isFooterPosition(position: Int):Boolean{
        return position >= getOriginalItemCount() + getHeadSize()
    }

    private fun isHeadPosition(position: Int):Boolean{
        return position < getHeadSize()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(heads.indexOfKey(viewType) >= 0){
            val view = heads[viewType]
            return object : RecyclerView.ViewHolder(view){}
        }
        if(foots.indexOfKey(viewType) >= 0){
            val view = foots[viewType]
            return object :RecyclerView.ViewHolder(view){}
        }
        val position = typePosition.get(viewType)
        val dataItem = dataSets.get(position)
        val vh = dataItem.onCreateViewHolder(parent)
        if(vh != null) return vh
        var itemView: View? = dataItem.getItemView(parent)
        if(itemView == null){
            val itemLayoutRes = dataItem.getItemLayoutRes()
            if(itemLayoutRes < 0){
                throw RuntimeException("dataItem ${dataItem.javaClass.name} must override getItemView() or getItemLayoutRes()")
            }
            itemView = mInflater!!.inflate(itemLayoutRes,parent,false)
        }
        return createViewHolderInternal(dataItem.javaClass,itemView)
    }

    private fun createViewHolderInternal(javaClass:Class<HiDataItem<*,out RecyclerView.ViewHolder>>,
    itemView: View?):RecyclerView.ViewHolder{
        val superclass = javaClass.superclass
        if(superclass is ParameterizedType){
            val arguments = superclass.actualTypeArguments
            for(argument in arguments){
                if(argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(argument)){
                    kotlin.runCatching {
                        return argument.getConstructor(View::class.java)
                            .newInstance(itemView) as ViewHolder
                    }.onFailure {
                        it.printStackTrace()
                    }
                }
            }
        }
        return object :HiViewHolder(itemView!!){}
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}