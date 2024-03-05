package com.example.hiui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.hiui.R

class MainActivity : AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_hi_taptop ->{
                startActivity(Intent(this, HiTabTopDemoActivity::class.java ))
            }
            R.id.tv_hi_refresh->{
                startActivity(Intent(this,HiRefreshDemoActivity::class.java))
            }
        }
    }



}