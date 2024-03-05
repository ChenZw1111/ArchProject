package empty

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.example.hiui.R
import java.util.jar.Attributes

class EmptyView : LinearLayout {
    private var icon: TextView
    private var desc: TextView
    private var button: Button

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    ){
        orientation = VERTICAL
        gravity = Gravity.CENTER

        LayoutInflater.from(context).inflate(R.layout.layout_empty_view,this,true)


        icon = findViewById(R.id.empty_icon)
        desc = findViewById(R.id.empty_text)
        button = findViewById(R.id.empty_action)

        var typeface:Typeface = Typeface.createFromAsset(context.assets,"fonts/iconfont.ttf")
    }

    fun setIcon(@StringRes iconRes:Int){
        icon.setText(iconRes)
    }

    fun setDesc(text:String){
        desc.text = text
        desc.visibility = if(TextUtils.isEmpty(text)) GONE else VISIBLE
    }

    fun setButton(text: String,listener: OnClickListener){
        if(TextUtils.isEmpty(text)){
            button.visibility = GONE
        }else{
            button.visibility = VISIBLE
            button.text = text
            button.setOnClickListener(listener)
        }
    }
}