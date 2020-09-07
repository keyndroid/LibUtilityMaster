package com.keyndroid.libutilitymaster.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.keyndroid.libutilitymaster.R
import java.io.File

class CustomImageView : RelativeLayout {
    private lateinit var mContext: Context
    private lateinit var view: View
    private lateinit var viewImage: ImageView
    private lateinit var pbImage: ProgressBar
    private var error: Drawable? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        val mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.view_image, this, true)

        pbImage = view.findViewById(R.id.pbImage) as ProgressBar
//        view.child;

        if (attrs != null) {
            val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageView)
            val hideLoader = typedArray.getBoolean(R.styleable.CustomImageView_hideLoader, false)
            error = typedArray.getDrawable(R.styleable.CustomImageView_error)
            updateImageView()

            if (hideLoader) {
                pbImage.visibility = View.GONE
            }
            typedArray.recycle()

        }
    }

    /**
     * Set iamge with imagepath
     */
    fun setImage(imagePath: String/*, @DrawableRes resourceId: Int*/) {
        updateImageView()
        val glide =  Glide.with(mContext).load(imagePath)
//            .error(resourceId)
//            .listener(requestListener).into(viewImage);
        setGlide(glide)
    }

    /**
     * Set iamge with File
     */
    fun setImage(imagePath: File/*, @DrawableRes resourceId: Int*/) {
        updateImageView()
        val glide = Glide.with(mContext).load(imagePath)
//            .error(resourceId)
//            .listener(requestListener).into(viewImage);
        setGlide(glide)

    }

    /**
     * Set iamge with Uri
     */
    fun setImage(imagePath: Uri/*, @DrawableRes resourceId: Int*/) {
        updateImageView()
        val glide = Glide.with(mContext).load(imagePath)
//            .error(resourceId)
//            .listener(requestListener).into(viewImage);
        setGlide(glide)
    }

    private fun updateImageView() {
        val count = childCount
        for (i in 0 until count){
            if (getChildAt(i) is ImageView){
                viewImage = getChildAt(i) as ImageView
                break
            }
        }

    }

    /**
     * Set iamge with resource
     */
    fun setImage(@DrawableRes imageResource: Int) {
        updateImageView()
        val glide = Glide.with(mContext).load(imageResource);
        setGlide(glide)
        /*glide.listener(requestListener).into(viewImage);*/

    }

    private fun setGlide(glide2: RequestBuilder<Drawable>){
        pbImage.visibility = View.VISIBLE
        var glide =glide2
        if (error != null) {
            glide = glide.error(error)
        }
        glide.listener(requestListener).into(viewImage);

    }

    private val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            pbImage.visibility = View.GONE

            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            pbImage.visibility = View.GONE
            return false
        }
    }

}
