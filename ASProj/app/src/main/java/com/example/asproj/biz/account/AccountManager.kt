package com.example.asproj.biz.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.asproj.LoginActivity
import com.example.asproj.cache.HiStorage
import com.example.asproj.model.UserProfile
import com.example.asproj.restful.HiCallback
import com.example.asproj.restful.HiResponse
import com.example.asproj.restful.api.AccountApi
import com.example.asproj.restful.http.ApiFactory
import com.example.common.utils.AppGlobals
import com.example.common.utils.SPUtil
import com.example.hilibrary.util.HiExecutor
import java.lang.IllegalStateException

object AccountManager {
    private var userProfile: UserProfile? = null

    @Volatile
    private var isFetching = false
    private var boardingPass: String? = null
    private val KEY_BOARDING_PASS = "boarding_pass"
    private val KEY_USER_PROFILE = "user_profile"
    private val loginLiveData = MutableLiveData<Boolean>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()
    private val userProfileLiveData = MutableLiveData<UserProfile?>()
    private val userProfileForeverObserver = mutableListOf<Observer<UserProfile?>>()

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context must not be null.")
        }
        context.startActivity(intent)
    }

    fun loginSuccess(boardingPass: String?) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        this.boardingPass = boardingPass
        loginLiveData.value = true
        clearLoginForeverObservers()
    }

    fun logout(){
        SPUtil.putString(KEY_BOARDING_PASS,"")
        this.boardingPass = null
        loginLiveData.value = false
        clearLoginForeverObservers()
    }
    private fun clearLoginForeverObservers() {
        for (observer in loginForeverObservers) {
            loginLiveData.removeObserver(observer)
        }
        loginForeverObservers.clear()
    }

    private fun clearProfileForeverObservers() {
        for (observer in userProfileForeverObserver) {
            userProfileLiveData.removeObserver(observer)
        }
        userProfileForeverObserver.clear()
    }

     fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }

    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {
        if (lifecycleOwner == null) {
            userProfileLiveData.observeForever(observer)
            userProfileForeverObserver.add(observer)
        } else {
            userProfileLiveData.observe(lifecycleOwner, observer)
        }

        if (userProfile != null && onlyCache) {
            userProfileLiveData.postValue(userProfile)
            return
        }

        if(isFetching){
            return
        }
        isFetching=true
        ApiFactory.create(AccountApi::class.java).getProfile()
            .enqueue(object :HiCallback<UserProfile>{
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    userProfile = response.data
                    if(response.code == HiResponse.SUCCESS &&
                            userProfile != null){
                        HiExecutor.execute(runnable = Runnable {
                            HiStorage.saveCache(KEY_USER_PROFILE, userProfile)
                        })
                        userProfileLiveData.value = userProfile
                    }else{
                        userProfileLiveData.value = null
                    }
                    clearProfileForeverObservers()
                    isFetching = false
                }

                override fun onFailed(throwable: Throwable) {
                    userProfileLiveData.value =null
                    clearProfileForeverObservers()
                    isFetching = false
                }
            })
    }

}