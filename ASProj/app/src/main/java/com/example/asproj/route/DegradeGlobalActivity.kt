package com.example.asproj.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.asproj.R
import com.example.hiui.icont.IconFontTextView
import empty.EmptyView
import empty.LostView

@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var degrade_title: String? = null

    @JvmField
    @Autowired
    var degrade_desc: String? = null

    @JvmField
    @Autowired
    var degrade_action: String? = null
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
//        setContentView(R.layout.layout_global_degrade)
//                val emptyView = findViewById<EmptyView>(R.id.empty_view)
//        emptyView.setIcon(R.string.if_unexpected1)
//        emptyView.setDesc(getString(R.string.degrade_text))
//        Toast.makeText(this,"oncreate",Toast.LENGTH_SHORT).show()
        ARouter.getInstance().inject(this)
        setContentView(R.layout.layout_global_degrade)
        val emptyView = findViewById<LostView>(R.id.empty_view)
        if (!TextUtils.isEmpty(degrade_title)) {
            emptyView.setTitle(degrade_title!!)
        }
        if (!TextUtils.isEmpty(degrade_desc)) {
            emptyView.setTitle(degrade_desc!!)
        }

        if (degrade_action != null) {
            emptyView.setHelpAction(listener =
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(degrade_action)
                        )
                    startActivity(intent)
                }
            })
        }
        val action_back = findViewById<IconFontTextView>(
            R.id.action_back
        ).setOnClickListener { onBackPressed() }

    }
}