package com.example.hiui.tab.bottom
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.example.hiui.R
import com.example.hiui.tab.common.IHiTab

class HiTabBottomG @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), IHiTab<HiTabBottomInfo<*>> {
    private var tabInfo: HiTabBottomInfo<*>? = null
    private var tabImageView: ImageView? = null
    private var tabIconView: TextView? = null
    private var tabNameView: TextView? = null

    fun getTabInfo(): HiTabBottomInfo<*>? {
        return tabInfo
    }
    init {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this)
        tabImageView = findViewById(R.id.iv_image)
        tabNameView = findViewById(R.id.tv_name)
        tabIconView = findViewById(R.id.tv_icon)
    }

    fun inflateInfo(selected: Boolean, init: Boolean): Unit {
        tabInfo?.let {
            if (it.tabType == HiTabBottomInfo.TabType.ICON) {
                if (init) {
                    tabImageView!!.visibility = GONE
                    tabIconView!!.visibility = VISIBLE
                    val typeface = Typeface.createFromAsset(
                        context.assets, it.iconFont
                    )
                    tabIconView!!.setTypeface(typeface)
                    if (!TextUtils.isEmpty(it.name)) {
                        tabNameView!!.text = it.name
                    }
                }
                if (selected) {
                    tabIconView!!.text =
                        if (TextUtils.isEmpty(it.selectedIconName)) it.defaultIconName else it.selectedIconName
                    tabIconView!!.setTextColor(getTextColor(it.tintColor))
                    tabNameView!!.setTextColor(getTextColor(it.tintColor))
                } else {
                    tabIconView!!.text = it.defaultIconName
                    tabIconView!!.setTextColor(getTextColor(it.defaultColor))
                    tabNameView!!.setTextColor(getTextColor(it.defaultColor))
                }
            } else if (it.tabType == HiTabBottomInfo.TabType.BITMAP) {
                if (init) {
                    tabImageView!!.visibility = VISIBLE
                    tabIconView!!.visibility = GONE
                    if (!TextUtils.isEmpty(it.name)) {
                        tabNameView!!.text = it.name
                    }
                }
                if (selected) {
                    tabImageView!!.setImageBitmap(it.selectedBitmap)
                } else {
                    tabImageView!!.setImageBitmap(it.defaultBitmap)
                }
            }
        }
    }

    override fun setHiTabInfo(data: HiTabBottomInfo<*>) {
        this.tabInfo = data
        inflateInfo(false, init = true)
    }

    override fun resetHeight(height: Int) {
    var layoutParams :ViewGroup.LayoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
        tabNameView!!.visibility = GONE
    }


    override fun onTabSelectedChange(
        index: Int,
        prevInfo: HiTabBottomInfo<*>?,
        nextInfo: HiTabBottomInfo<*>?
    ) {
        if(prevInfo !=tabInfo && nextInfo != tabInfo || prevInfo == nextInfo){
            return
        }
        if(prevInfo == tabInfo){
            inflateInfo(false,init = true)
        }else{
            inflateInfo(true, init = false)
        }
    }

    @ColorInt
    fun getTextColor(color: Any): Int {
        return if (color is String) {
            Color.parseColor(color as String)
        } else {
            color as Int
        }
    }
}