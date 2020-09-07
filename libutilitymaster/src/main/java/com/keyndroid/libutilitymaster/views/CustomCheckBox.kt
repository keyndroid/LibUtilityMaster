package com.keyndroid.libutilitymaster.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.keyndroid.libutilitymaster.R
import com.keyndroid.libutilitymaster.utils.LibUtil

class CustomCheckBox : AppCompatCheckBox {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckBox)
        val fontName = typedArray.getString(R.styleable.CustomTextView_customFont)
//        typeface =
            LibUtil.setFont(this,context,fontName)
        typedArray.recycle()
    }
}

