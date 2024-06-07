package com.example.hiui.refresh

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.hilibrary.log.HiLog
import com.example.hilibrary.util.HiDisplayUtil

/**
 * 下拉刷新的Overlay视图，可以重载这个类
 * head
 */
abstract class HiOverView : FrameLayout {
    enum class HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,

        /**
         * Header展示状态
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,

        /**
         * 刷新中的状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE
    }
    /**
     * 获取状态
     */
    /**
     * 设置状态
     */
    var state = HiRefreshState.STATE_INIT

    /**
     * 触发下拉刷新 需要的最小高度
     */
    @JvmField
    var mPullRefreshHeight = 0

    /**
     * 最小阻尼：视图与手势的比例系数
     */
    @JvmField
    var minDamp = 1.6f

    /**
     * 最大阻尼
     */
    @JvmField
    var maxDamp = 2.2f

    constructor(context: Context) : super(context) {
        preInit()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        preInit()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        preInit()
    }

    protected fun preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66f, resources)
        HiLog.e("HiRefreshLayout", "mPullRefreshHeight :$mPullRefreshHeight")
        init()
    }

    /**
     * 初始化
     */
    abstract fun init()
    abstract fun onScroll(scrollY: Int, mPullRefreshHeight: Int)

    /**
     * 显示Overlay
     */
    abstract fun onVisible()

    /**
     * 超过Overlay,释放就会加载
     */
    abstract fun onOver()

    /**
     * 开始刷新
     */
    abstract fun onRefresh()

    /**
     * 刷新完成
     */
    abstract fun onFinish()
}