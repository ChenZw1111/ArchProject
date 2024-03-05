package com.example.hiui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hilibrary.log.HiLog
import com.example.hiui.R
import com.example.hiui.refresh.HiLottieOverView
import com.example.hiui.refresh.HiRefresh
import com.example.hiui.refresh.HiRefreshLayout
import com.example.hiui.refresh.HiTextOverView

class HiRefreshDemoActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh_demo)
        val refreshLayout = findViewById<HiRefreshLayout>(R.id.refresh_layout)
        val lottieOverView = HiLottieOverView(this)
        val xOverView = HiTextOverView(this)
        refreshLayout.setRefreshOverView(xOverView)
        refreshLayout.setRefreshListener(object : HiRefresh.HiRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({ refreshLayout.refreshFinished() }, 1000)
            }

            override fun enableRefresh(): Boolean {
                return true
            }
        })
        refreshLayout.setDisableRefreshScroll(false)
        initRecycleView()
    }

    var myDataset =
        arrayOf("HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh")

    private fun initRecycleView() {
        recyclerView = findViewById<RecyclerView>(R.id.recycleview)
        recyclerView!!.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        val mAdapter = MyAdapter(myDataset)
        recyclerView!!.adapter = mAdapter
    }

    class MyAdapter(

        private val mDataset: Array<String>
    ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        class MyViewHolder(v: View) : ViewHolder(v) {
            var textView: TextView

            init {
                textView = v.findViewById(R.id.tv_title)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = mDataset[position]
            holder.itemView.setOnClickListener { HiLog.d("positon:$position") }
        }

        override fun getItemCount(): Int {
            return mDataset.size
        }
    }
}