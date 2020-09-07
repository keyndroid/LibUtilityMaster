package com.keyndroid.libutilitymaster.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.keyndroid.libutilitymaster.R


class CustomRadioButton : AppCompatRadioButton {

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
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRadioButton)
        val fontName = typedArray.getString(R.styleable.CustomTextView_customFont)
        val customTypeface = Typeface.createFromAsset(context.assets, fontName)
        typeface = customTypeface
        typedArray.recycle()
    }
}