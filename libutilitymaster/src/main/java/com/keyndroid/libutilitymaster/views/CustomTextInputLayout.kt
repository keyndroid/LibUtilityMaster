package com.keyndroid.libutilitymaster.views

import android.content.Context
import android.util.AttributeSet


import com.google.android.material.textfield.TextInputLayout
import com.keyndroid.libutilitymaster.R
import com.keyndroid.libutilitymaster.utils.LibUtil

class CustomTextInputLayout : TextInputLayout {

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
        val typedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextInputLayout)
        val fontName = typedArray.getString(R.styleable.CustomTextView_customFont)

        LibUtil.setFont(this,context,fontName)
        typedArray.recycle()
    }

}