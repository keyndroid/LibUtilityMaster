package com.keyndroid.libutilitymaster.utils

import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.BuildConfig
import java.util.regex.Pattern

/**
 * Created by Keyur on 09,January,2020 11:24 AM
 */
object LoggerUtils {
    private val CALL_STACK_INDEX = 1
    private val ANONYMOUS_CLASS =
        Pattern.compile("(\\$\\d+)+$")
    private val MAX_TAG_LENGTH = 23
    private var mTag:String? = null

    fun tag(tag:String): LoggerUtils {
        this.mTag=tag
        return LoggerUtils
    }
    fun e(message: String){
        if (!TextUtils.isEmpty(message) && BuildConfig.DEBUG){
            // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
// because Robolectric runs them on the JVM but on Android the elements are different.
            val stackTrace =
                Throwable().stackTrace
            check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
            var tag=   mTag
            if (TextUtils.isEmpty(mTag)){
                tag=   createStackElementTag(stackTrace[CALL_STACK_INDEX])
            }
            Log.e(tag, message)
        }

        mTag=null
    }
    fun createStackElementTag(element: StackTraceElement): String? {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else tag.substring(0, MAX_TAG_LENGTH)
    }


}
