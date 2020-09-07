package com.keyndroid.libutilitymaster.views

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatSpinner

class CustomSpinner : AppCompatSpinner {
    lateinit var onDropDownOpened: OnDropDownOpened

    constructor(context: Context) : super(context) {}

    constructor(context: Context, mode: Int) : super(context, mode) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        mode: Int
    ) : super(context, attrs, defStyleAttr, mode) {
    }


    override fun performClick(): Boolean {
        onDropDownOpened.onDropDownOpened()
        return super.performClick()
    }

    interface OnDropDownOpened {
        fun onDropDownOpened()
    }
}
