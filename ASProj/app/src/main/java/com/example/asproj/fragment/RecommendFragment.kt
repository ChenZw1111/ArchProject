package com.example.asproj.fragment

import android.os.Bundle
import android.view.View
import com.example.common.flutter.HiFlutterCacheManager
import com.example.common.flutter.HiFlutterFragment

class RecommendFragment : HiFlutterFragment() {
    override val moduleName: String?
        get() = HiFlutterCacheManager.MODULE_NAME_RECOMMEND

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("推荐")
    }
}