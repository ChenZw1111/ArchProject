package com.example.hiui.input

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.LEFT
import android.view.Gravity.START
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hiui.R

class InputItemLayout : LinearLayout {

    private lateinit var titleView: TextView
    private lateinit var ediText: EditText
    private var topLine: Line
    private var bottomLine: Line
    private var topPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context, attributeSet, defStyle
    ) {
        orientation = HORIZONTAL
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)

        val title = array.getString(R.styleable.InputItemLayout_title)
        val titleResId = array.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        parseTitleStyle(title, titleResId)

        val hint = array.getString(R.styleable.InputItemLayout_hint)
        val inputResId = array.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val inputType = array.getInt(R.styleable.InputItemLayout_inputType, 0)
        parseInputStyle(hint, inputResId, inputType)

        val topResId = array.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomResId = array.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)
        topLine = parseLineStyle(topResId)
        bottomLine = parseLineStyle(bottomResId)

        if (topLine.enable) {
            topPaint.color = topLine.color
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.strokeWidth = topLine.height
        }

        if (bottomLine.enable) {
            bottomPaint.color = bottomLine.color
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.strokeWidth = bottomLine.height
        }
        array.recycle()
    }

    inner class Line {
        var color = 0
        var height = 0f
        var leftMargin = 0f
        var rightMargin = 0f
        var enable: Boolean = false
    }

    private fun parseLineStyle(resId: Int): Line {
        val line = Line()
        val array = context.obtainStyledAttributes(resId, R.styleable.lineAppearance)
        line.color = array.getColor(
            R.styleable.lineAppearance_color,
            ContextCompat.getColor(context, R.color.color_d1d2)
        )
        line.height = array.getDimensionPixelOffset(R.styleable.lineAppearance_height, 0).toFloat()
        line.leftMargin = array.getDimensionPixelOffset(
            R.styleable.lineAppearance_leftMargin,
            0
        ).toFloat()
        line.rightMargin = array.getDimensionPixelOffset(
            R.styleable.lineAppearance_rightMargin, 0
        ).toFloat()
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)
        array.recycle()
        return line
    }

    private fun parseInputStyle(hint: String?, resId: Int, inputType: Int) {
        val array = context.obtainStyledAttributes(resId, R.styleable.inputTextAppearance)

//        <declare-styleable name="inputTextAppearance">
//        <attr name="hintColor" format="color" />
//        <attr name="inputColor" format="color" />
//        <attr name="textSize" format="dimension" />
//        <attr name="maxInputLength" format="integer" />
//        </declare-styleable>
        val hintColor = array.getColor(
            R.styleable.inputTextAppearance_hintColor,
            ContextCompat.getColor(context, R.color.color_d1d2)
        )

        val inputColor = array.getColor(
            R.styleable.inputTextAppearance_inputColor,
            ContextCompat.getColor(context, R.color.color_565)
        )

        val textSize = array.getDimensionPixelSize(
            R.styleable.inputTextAppearance_textSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )

        val maxInputLength = array.getInteger(R.styleable.inputTextAppearance_maxInputLength, 0)

        ediText = EditText(context)
        if (maxInputLength > 0) {
            ediText.filters = arrayOf(InputFilter.LengthFilter(maxInputLength))
        }
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        ediText.layoutParams = params
        ediText.hint = hint
        ediText.setTextColor(inputColor)
        ediText.setHintTextColor(hintColor)
        ediText.setBackgroundColor(Color.TRANSPARENT)
        ediText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize.toFloat())
        ediText.gravity = START and (CENTER)
        if (inputType == 0) {
            ediText.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputType == 1) {
            ediText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or(InputType.TYPE_CLASS_TEXT)
        } else if (inputType == 2) {
            ediText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        addView(ediText)
        array.recycle()
    }


    //    <declare-styleable name="titleTextAppearance">
//    <attr name="titleColor" format="color" />
//    <attr name="titleSize" format="dimension" />
//    <attr name="minWidth" format="dimension" />
//    </declare-styleable>
    private fun parseTitleStyle(title: String?, resId: Int) {
        val array = context.obtainStyledAttributes(resId, R.styleable.titleTextAppearance)
        val titleColor = array.getColor(
            R.styleable.titleTextAppearance_titleColor,
            resources.getColor(R.color.color_565, null)
        )
        val titleSize = array.getDimensionPixelSize(
            R.styleable.titleTextAppearance_titleSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )
        val minWidth = array.getDimensionPixelOffset(R.styleable.titleTextAppearance_minWidth, 0)
        titleView = TextView(context)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        titleView.setTextColor(titleColor)
        titleView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        titleView.minWidth = minWidth
        titleView.gravity = START or(CENTER)
        titleView.text = title
        addView(titleView)

        array.recycle()
    }

    private fun applyUnit(applyUnit: Int, value: Float): Int {
        return TypedValue.applyDimension(applyUnit, value, resources.displayMetrics).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(topLine.enable){
            canvas!!.drawLine(
                topLine.leftMargin,
                0f,
                measuredWidth - topLine.rightMargin,
                0f,
                topPaint
            )
        }

        if(bottomLine.enable){
            canvas!!.drawLine(
                bottomLine.leftMargin,
                height - bottomLine.height,
                measuredWidth - bottomLine.leftMargin,
                height - bottomLine.height,
                bottomPaint
            )
        }
    }

    fun getEditText():EditText{
        return ediText
    }
}