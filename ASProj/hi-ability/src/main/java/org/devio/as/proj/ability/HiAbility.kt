package org.devio.`as`.proj.ability

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.huawei.hms.ml.scan.HmsScan
import org.devio.`as`.proj.ability.push.IPushMessageHandler
import org.devio.`as`.proj.ability.push.PushInitialization
import org.devio.`as`.proj.ability.scan.ScanActivity
import org.devio.`as`.proj.ability.share.ShareBundle
import org.devio.`as`.proj.ability.share.ShareManager

object HiAbility {
    private val scanResultLiveData = MutableLiveData<String>()
    fun init(
        application: Application,
        channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        PushInitialization.init(application, channel, iPushMessageHandler)
    }

    fun openScanActivity(activity: Activity, observer: Observer<String>) {
        if (activity is LifecycleOwner) {
            scanResultLiveData.observe(activity, observer)
        }
        activity.startActivity(Intent(activity, ScanActivity::class.java))
    }

    fun openShareDialog(context: Context, shareBundle: ShareBundle) {
        ShareManager.share(context, shareBundle)
    }

    fun onScanResult(hmsScan: HmsScan) {
        if (hmsScan.getScanTypeForm() == HmsScan.SMS_FORM) {
            // 解析成SMS的结构化数据
            val smsContent = hmsScan.getSmsContent()
            val content = smsContent.getMsgContent()
            val phoneNumber = smsContent.getDestPhoneNumber()
            scanResultLiveData.postValue(phoneNumber)
        } else if (hmsScan.getScanTypeForm() == HmsScan.WIFI_CONNECT_INFO_FORM) {
            // 解析成Wi-Fi的结构化数据
            val wifiConnectionInfo = hmsScan.wiFiConnectionInfo
            val password = wifiConnectionInfo.getPassword()
            val ssidNumber = wifiConnectionInfo.getSsidNumber()
            val cipherMode = wifiConnectionInfo.getCipherMode()
            scanResultLiveData.postValue(ssidNumber)
        } else {
            scanResultLiveData.postValue(hmsScan.originalValue)
        }
        scanResultLiveData.value = null
    }
}