package com.keyndroid.libutilitymaster.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.*
import com.bumptech.glide.BuildConfig
import com.google.android.material.textfield.TextInputLayout
import com.keyndroid.libutilitymaster.R
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object LibUtil {
    private val CALL_STACK_INDEX = 1
    private val ANONYMOUS_CLASS =
        Pattern.compile("(\\$\\d+)+$")
    private val MAX_TAG_LENGTH = 23
    private var mTag:String? = null

    fun tag(tag:String): LibUtil {
        this.mTag=tag
        return LibUtil
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

    val randomOTP: String
        get() {
            val random = Random()
            return (100000 + random.nextInt(900000)).toString()
        }


    val isRTL: Boolean
        get() = isRTL(Locale.getDefault())

    /**
     * Sets size.
     * Below two lines refers same method.
     * @param email email Address for validation
     * @see MaterialUtility#showMenu
     * @see #isEmail(email: String)
     */
    fun isEmail(email: String): Boolean {

        val emailPattern =
            Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        return if (emailPattern.matcher(email).find()) true else false

    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun imageToString(BitmapData: Bitmap): String {

        val bos = ByteArrayOutputStream()
        BitmapData.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val byte_arr = bos.toByteArray()

//appendLog(file);
        return Base64.encodeToString(byte_arr, Base64.DEFAULT)
    }

    fun checkIsMarshMallow(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) true else false
    }


    fun whichPermisionNotGranted(
        context: Context,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                showToast(context, "Authentication Permission Not Enabled")
            }
        }
    }

    fun getConvertDate(sourceFormat: String, destFormat: String, strDate: String): String {
        var finalDate = ""
        try {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val srcDf = SimpleDateFormat(sourceFormat, locale)
            // parse the date string into Date object
            val date = srcDf.parse(strDate)
            val destDf = SimpleDateFormat(destFormat, locale)
            // format the date into another format
            finalDate = destDf.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return finalDate

    }

    @SuppressLint("MissingPermission")
    fun isInternetAvailable(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo

        return if (netInfo != null && netInfo.isConnected && netInfo.isAvailable) true else false

    }


    fun getDateTimeStamp(format: String, date: String): Long {
        var timeStamp: Long = 0
        val locale = Locale("en")
        Locale.setDefault(locale)
        val formatter = SimpleDateFormat(format, locale)
        var mDate: Date? = null
        try {
            mDate = formatter.parse(date)
            timeStamp = mDate!!.time
        } catch (e: ParseException) {
            timeStamp = 0
            e.printStackTrace()
        }

        return timeStamp
    }

    fun getCurrentTimeStamp(format: String): Long {

        val date = getDateFromTimeStamp(System.currentTimeMillis(), format)
        return getDateTimeStamp(format, date)
    }


    fun getDateFromTimeStamp(timeStamp: Long, dateFormat: String): String {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val objFormatter = SimpleDateFormat(dateFormat, locale)
        val objCalendar = Calendar.getInstance(locale)
        objCalendar.timeInMillis = timeStamp
        val result = objFormatter.format(objCalendar.time)
        objCalendar.clear()
        return result
    }

    fun getCurrentDatePlusMonth(monthIncrementCounter: Int): String {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val cal = Calendar.getInstance(locale)
        cal.time = Date()
        cal.add(Calendar.MONTH, monthIncrementCounter)

        val month_date = SimpleDateFormat("MMM", locale)
        return month_date.format(cal.time)
    }


    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = listView.paddingTop + listView.paddingBottom
        val desiredWidth =
            View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                totalHeight += listItem.measuredHeight

            }
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getDeviceWidth(activity: Activity): Int {

        val wm = activity.windowManager
        val point = Point()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.defaultDisplay.getSize(point)
            return point.x
        } else {
            return wm.defaultDisplay.width
        }
    }

    fun getDeviceHeight(activity: Activity): Int {
        val wm = activity.windowManager
        val point = Point()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.defaultDisplay.getSize(point)
            return point.y
        } else {
            return wm.defaultDisplay.height
        }
    }

    fun isTablet(context: Context): Boolean {
        val xlarge =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val large =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }

    fun openKeyBoard(context: Context) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun closeKeyBoard(activity: Context) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun hideKeyboard(context: Context) {
        try {
            val activity = context as Activity

            val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val focus = activity.currentFocus
            if (focus != null)
                inputManager.hideSoftInputFromWindow(
                    focus.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        } catch (e: Exception) {

        }

    }

    fun showKeyboard(context: Context) {
        val activity = context as Activity

        val inputManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focus = activity.currentFocus
        if (focus != null)
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun getDisplayWidth(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        return displayMetrics.widthPixels
    }

    fun getDisplayHeight(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return height
    }

    inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }

    fun startInstalledAppDetailsActivity(context: Context?) {
        if (context == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(i)
    }

    fun isRTL(context: Context): Boolean {
        val configuration = context.resources.configuration
        return if (configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            true
        } else {
            false
        }
    }

    fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }

    fun setViewBasedOnRtl(v: View, context: Context) {
        val l = getCurrentLocale(context)
        if (l.language.equals("ar", ignoreCase = true)) {
            v.scaleY = -1.0f
            v.rotation = 180f
        } else {
            v.scaleY = 0f
            v.rotation = 0f
        }

    }

    fun setNotificationViewBasedOnRtl(v: View, context: Context) {
        val l = getCurrentLocale(context)
        if (l.language.equals("ar", ignoreCase = true)) {
            v.rotation = 90.0f
        } else {
            v.rotation = 270.0f
        }

    }

    fun getCurrentLocale(context: Context): Locale {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {

            context.resources.configuration.locale
        }


    }


    fun getDigitsString(digit: Int): String {
        return if (digit > 9) {
            "" + digit
        } else {
            "0$digit"
        }

    }

    fun getYearFromDate(birthdate: String): Int {
        val cal = Calendar.getInstance()
        val time = LibUtil.getDateTimeStamp("yyyy-MM-dd", birthdate)
        cal.timeInMillis = time
        val dobYear = cal.get(Calendar.YEAR)
        val curYear = Calendar.getInstance().get(Calendar.YEAR)
        var diff = curYear - dobYear
        if (diff < 0) {
            diff = 0
        }
        return diff
    }

    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

    fun setSpinnersDropDownHeight(context: Context, spinner: Spinner, listSize: Int) {
        if (listSize > 4) {
            try {
                val popup = Spinner::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true

                // Get private mPopup member variable and try cast to ListPopupWindow
                val popupWindow = popup.get(spinner) as android.widget.ListPopupWindow

                popupWindow.height = LibUtil.convertDpToPixel(150f, context).toInt()

            } catch (e: NoClassDefFoundError) {
                // silently fail...
            } catch (e: ClassCastException) {
            } catch (e: NoSuchFieldException) {
            } catch (e: IllegalAccessException) {
            }

        }
    }

    fun getRating(amount: Float): String {
        return String.format("%.1f", amount)
    }

    fun getCurrencyFill(price: String): Spanned {
        //Sample font string here
        //String text = "<font color=#00dc99>First Color</font> <font color=#ffcc00>Second Color</font>";
        val fillString = StringBuilder()
        val maxCount = 3

        var minPrice = 0f
        try {
            minPrice = java.lang.Float.parseFloat(price)
        } catch (e: NullPointerException) {
            minPrice = 0f
        } catch (e: NumberFormatException) {
            minPrice = 0f
        }

        val fillColor: Int

        if (minPrice < 10) {
            fillColor = 1
        } else if (minPrice < 100) {
            fillColor = 2
        } else {
            fillColor = 3
        }

        for (i in 0 until maxCount) {
            if (i < fillColor) {
                fillString.append("<font color=#00dc99>")
            } else {
                fillString.append("<font color=#aaaaaa>")
            }

            fillString.append("$")
            if (i - 1 != maxCount) {
                fillString.append(" ")
            }
            fillString.append("</font> ")
        }
        return Html.fromHtml(fillString.toString())
    }

    fun getScreenWidth(context: Context): Int {

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x
    }

    private fun getFont(context: Context, asd: String?): Typeface
        {
            if (TextUtils.isEmpty(asd)){
                return Typeface.createFromAsset(context.assets, asd)
            }else{
                return Typeface.createFromAsset(context.assets, context.resources.getString(R.string.font_regular))
            }

        }
    fun setFont(view: View, context: Context, fontName: String?) {
        if (TextUtils.isEmpty(fontName)){
            return
        }
        when(view){
            is AppCompatTextView -> view.setTypeface(getFont(context, fontName))
            is AppCompatEditText -> view.setTypeface(getFont(context, fontName))
            is AppCompatCheckBox -> view.setTypeface(getFont(context, fontName))
            is AppCompatButton -> view.setTypeface(getFont(context, fontName))
            is AppCompatRadioButton -> view.setTypeface(getFont(context, fontName))
            is TextInputLayout -> view.setTypeface(getFont(context, fontName))
            /*else -> {
                throw IllegalStateException("Font view is not defined in ")
            }*/
        }
//    view.settype
    }
    fun logger(msg: String) {
        if (!BuildConfig.FLAVOR.contains("live") && BuildConfig.DEBUG) {
            Log.e("MyLogger", msg)
        }
    }


}
