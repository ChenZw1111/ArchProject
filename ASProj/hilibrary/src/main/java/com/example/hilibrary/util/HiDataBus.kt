package com.example.hilibrary.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap

object HiDataBus {
    private var mEventMap = ConcurrentHashMap<String, StickyLiveData<*>>()
    fun <T> with(eventName: String): StickyLiveData<T> {
        var liveData = mEventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            mEventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(val mEventName:String) : LiveData<T>() {
        var mStickyData: T? = null
        var mVersion = 0

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        fun setStickyData(stickyData: T) {
            mStickyData = stickyData
            setValue(stickyData)
        }

        fun postStickyData(stickyData: T) {
            mStickyData = stickyData
            postValue(stickyData)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observeSticky(owner, observer,false)
        }
        fun observeSticky(owner: LifecycleOwner, observer: Observer<in T>, sticky: Boolean) {
            owner.lifecycle.addObserver(LifecycleEventObserver{source, event ->
                if(event == Lifecycle.Event.ON_DESTROY){
                    mEventMap.remove(mEventName)
                }
            })
            super.observe(owner,StickyObserver(this, observer, sticky))

        }

        internal class StickyObserver<T>(
            val stickyLiveData: StickyLiveData<T>,
            val observer:Observer<in T>,
            val sticky:Boolean
        ):Observer<T>{
            private var mLastVersion = 0
            override fun onChanged(t: T) {
                if(mLastVersion >= stickyLiveData.mVersion){
                    if(sticky && stickyLiveData.mStickyData != null){
                        observer.onChanged(stickyLiveData.mStickyData)
                    }
                    return
                }

                mLastVersion = stickyLiveData.mVersion
                observer.onChanged(t)
            }
        }
    }

}
