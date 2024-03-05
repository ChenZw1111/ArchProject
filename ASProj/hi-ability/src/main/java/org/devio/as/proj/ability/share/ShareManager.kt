package org.devio.`as`.proj.ability.share

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.devio.`as`.proj.ability.BuildConfig


object ShareManager {
    const val CHANNEL_QQ = "com.tencent.mobileqq.activity.JumpActivity"
    fun share(context: Context, shareBundle: ShareBundle) {
        val localChannels = queryLocalChannels(context, shareBundle.type)
        val pm = context.packageManager
        val shareChannels = arrayListOf<ResolveInfo>()
        if (shareBundle.channels != null) {
            for (localChannel in localChannels) {
                val localLabel = localChannel.loadLabel(pm)
                for (channel in shareBundle.channels!!) {
                    if (TextUtils.equals(channel, localLabel)) {
                        shareChannels.add(localChannel)
                    }
                }
            }
        } else {
            shareChannels.addAll(localChannels)
        }
        val shareDialog = ShareDialog(context)
        shareDialog.setChannels(shareChannels, object : ShareDialog.onShareChannelClickListener {
            override fun onClickChannel(resolveInfo: ResolveInfo) {
                shareToChannel(context, resolveInfo,shareBundle)
            }
        })
        shareDialog.show()
    }

    private fun shareToChannel(context: Context, resolveInfo: ResolveInfo, shareBundle: ShareBundle) {
        val name = resolveInfo.activityInfo.name
        when (name) {
            CHANNEL_QQ -> {
                share2QQFriend(context,shareBundle)
            }
        }
    }

    private fun share2QQFriend(context: Context, shareBundle: ShareBundle) {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareBundle.title)
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBundle.summary)
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBundle.targetUrl)
        params.putString(
            QQShare.SHARE_TO_QQ_IMAGE_URL,
            shareBundle.thumbUrl
        )
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareBundle.appName)
        val mTencent = Tencent.createInstance(BuildConfig.QQ_SHARE_APP_ID, context)
        val activity = findActivity(context)
        mTencent.shareToQQ(activity, params, object :IUiListener{
            override fun onComplete(p0: Any?) {
                showToast(context,"分享成功")
            }

            override fun onError(p0: UiError?) {
                p0?.apply {
                    showToast(context,p0.errorMessage)
                }
            }

            override fun onCancel() {
                showToast(context,"取消分享")
            }

            override fun onWarning(p0: Int) {
            }

        })
    }

    private fun queryLocalChannels(context: Context, shareType: String): List<ResolveInfo> {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = shareType

        val resolveInfo: ArrayList<ResolveInfo> = arrayListOf<ResolveInfo>()

        val pm = context.packageManager
        val activities = pm.queryIntentActivities(intent, 0)
        for (activity in activities) {
            val packageName = activity.activityInfo.packageName
            if (TextUtils.equals(packageName, "com.tencent.mm") || TextUtils.equals(
                    packageName,
                    "com.tencent.mobileqq"
                )
            ) {
                resolveInfo.add(activity)
            }
        }
        return resolveInfo
    }

    fun findActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return findActivity(context.baseContext)
        }
        return null
    }

    fun showToast(context: Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}