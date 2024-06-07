package com.example.asproj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.asproj.biz.account.AccountManager
import com.example.asproj.restful.HiCallback
import com.example.asproj.restful.HiResponse
import com.example.asproj.restful.api.AccountApi
import com.example.asproj.restful.http.ApiFactory
import com.example.common.HiBaseActivity
import com.example.common.utils.SPUtil
import com.example.hiui.icont.IconFontTextView
import com.example.hiui.input.InputItemLayout
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
    private val REQUEST_CODE_REGISTRATION = 1000
    private val TAG: String = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
    }

    private fun initView() {
        findViewById<IconFontTextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }
        findViewById<TextView>(R.id.title_register).setOnClickListener {
            goRegistration()
        }
        findViewById<MaterialButton>(R.id.action_login).setOnClickListener {
            goLogin()
        }
        findViewById<MaterialButton>(R.id.action_thread).setOnClickListener {
            lifecycleScope.launch {
                val fileContent = parseAssetsFile()
                Log.e(TAG, fileContent)
            }
            Log.e(TAG, "主线程。。")
        }
    }

    suspend fun parseAssetsFile(): String {
        delay(2000)
        print("after delay")
        return "result from parse"
    }

    private fun goLogin() {
        val name =
            findViewById<InputItemLayout>(R.id.input_item_username).getEditText().text.toString()
        val pwd =
            findViewById<InputItemLayout>(R.id.input_item_password).getEditText().text.toString()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            return
        }

        ApiFactory.create(AccountApi::class.java).login(name, pwd)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    showToast("登陆成功")
                    val data = response.data
//                    SPUtil.putString("boarding-pass",data!!)
                    Log.e(TAG,Thread.currentThread().name)
                    AccountManager.loginSuccess(data!!)
                    setResult(Activity.RESULT_OK,Intent())
                    finish()
                }

                override fun onFailed(throwable: Throwable) {
                    throwable.printStackTrace()
                    showToast("登陆失败")
                    Log.e(TAG,Thread.currentThread().name)
                }
            })

    }

    private fun showToast(message: String) {
        if (message.isEmpty()) return
        Toast.makeText(
            applicationContext,
            message, Toast.LENGTH_SHORT
        ).show()
    }

    private fun goRegistration() {
        ARouter.getInstance().build("/account/registration")
            .navigation(this,REQUEST_CODE_REGISTRATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null) and (requestCode == REQUEST_CODE_REGISTRATION)) {
            val username = data!!.getStringExtra("username")
            if (TextUtils.isEmpty(username)) {
                findViewById<InputItemLayout>(R.id.input_item_username).getEditText()
                    .setText(username)
            }
        }
    }
}