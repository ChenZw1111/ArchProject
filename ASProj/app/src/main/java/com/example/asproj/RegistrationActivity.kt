package com.example.asproj

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.asproj.restful.HiCallback
import com.example.asproj.restful.HiResponse
import com.example.asproj.restful.api.AccountApi
import com.example.asproj.restful.http.ApiFactory
import com.example.common.HiBaseActivity
import com.example.hiui.icont.IconFontTextView
import com.example.hiui.input.InputItemLayout
import com.google.android.material.button.MaterialButton

@Route(path = "/account/registration")
class RegistrationActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<IconFontTextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }

        findViewById<MaterialButton>(R.id.action_submit).setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        // 提交注册信息
        val username =
            findViewById<InputItemLayout>(R.id.input_item_username).getEditText().editableText.toString()
        val phoneNum =
            findViewById<InputItemLayout>(R.id.input_item_phoneNum).getEditText().editableText.toString()
        val password =
            findViewById<InputItemLayout>(R.id.input_item_pwd).getEditText().editableText.toString()
        val confirmPassword =
            findViewById<InputItemLayout>(R.id.input_item_pwd_check).getEditText().editableText.toString()
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNum.isEmpty()
            || password != confirmPassword
        ) {
            return
        }

        ApiFactory.create(AccountApi::class.java).register(username, password, phoneNum)
            .enqueue(object : HiCallback<String> {

                override fun onSuccess(response: HiResponse<String>) {
                    showToast("注册成功")
                    intent.putExtra("username", username)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message.toString())
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
}