package com.example.common.flutter

interface IHiBridge<P,CallBack> {
    fun onBack(p:P?)
    fun goToNative(p:P)
    fun getHeaderParams(callBack: CallBack)
}