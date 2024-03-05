package com.example.common.flutter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.common.HiBaseFragment
import com.example.common.R
import com.example.common.databinding.LayoutFlutterBinding
import com.example.common.utils.AppGlobals
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

abstract class HiFlutterFragment : HiBaseFragment() {
    protected val flutterEngine: FlutterEngine?
    protected var flutterView: FlutterView? = null
    private var _binding: LayoutFlutterBinding? = null
    abstract val moduleName: String?

    private val binding get() = _binding!!

    init {
        flutterEngine = HiFlutterCacheManager.instance!!.getCacheFlutterEngine(
            moduleName!!,
            AppGlobals.get()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutFlutterBinding.inflate(inflater, container, false)
//        layoutView = inflater.inflate(R.layout.layout_flutter,container,false)
        layoutView = binding.root
        return layoutView
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_flutter
    }

    fun setTitle(titleStr: String) {
        binding.title.text = titleStr
        binding.title.setOnClickListener {
            HiFlutterBridge.instance!!.fire(
                "onRefresh", "easy", object : MethodChannel.Result {
                    override fun success(result: Any?) {
                        Toast.makeText(context, result as String, Toast.LENGTH_SHORT).show()
                    }

                    override fun error(
                        errorCode: String,
                        errorMessage: String?,
                        errorDetails: Any?
                    ) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun notImplemented() {
                    }
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (layoutView as ViewGroup).addView(createFlutterView(requireActivity()))
    }

    private fun createFlutterView(context: Context): FlutterView {
        val flutterTextureView = FlutterTextureView(requireActivity())
        flutterView = FlutterView(context, flutterTextureView)
        return flutterView!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        flutterView!!.attachToFlutterEngine(flutterEngine!!)
    }

    override fun onResume() {
        super.onResume()
        flutterEngine!!.lifecycleChannel.appIsResumed()
    }

    override fun onPause() {
        super.onPause()
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }

    override fun onStop() {
        super.onStop()
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }

    override fun onDetach() {
        super.onDetach()
        flutterEngine!!.lifecycleChannel.appIsDetached()
    }
}