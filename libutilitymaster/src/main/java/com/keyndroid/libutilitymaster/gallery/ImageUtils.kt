package com.keyndroid.libutilitymaster.gallery

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import com.keyndroid.libutilitymaster.utils.FOLDER_NAME
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Keyur on 23,September,2019
 */
internal class ImageUtils {
    companion object {
        val IMAGE_EXTENSION = ".jpg"
    }

    fun getFileSizeKiloBytes(file: File): Double? {
        return file.length().toDouble() / 1024/*.toString()*/ /*+ "  kb"*/
    }
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir = getMyFolderPath(context)
//        val directory= File(storageDir)
        if (!storageDir.exists() || !storageDir.isDirectory) {
            storageDir.mkdirs()
        }
        val image =//File(storageDir.absolutePath+File.separator+imageFileName+ IMAGE_EXTENSION)
            File.createTempFile(
                imageFileName, /* prefix */
                IMAGE_EXTENSION, /* suffix */
                storageDir   /* directory */
            )

//        imageFilePath = image.absolutePath
        return image
    }

    fun getMyFolderPath(context: Context): File {
        return File(getDataRoot(context), FOLDER_NAME)
    }

    private fun getDataRoot(context: Context): File? {
        try {
            /*if (BuildConfig.DEBUG){
                return Environment.getDataDirectory()
            }*/
            /*return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || Environment.getExternalStorageDirectory().exists() && Environment.getExternalStorageDirectory().canWrite()) {
                Environment.getExternalStorageDirectory()
            } else {
                Environment.getDataDirectory()
            }*/
            return context.getExternalFilesDir("Cow")
        } catch (e: Exception) {
            return null
        }

    }

    //    val filename: String
//        get() {
//            val file = File(Environment.getExternalStorageDirectory().path, "MyFolder/Images")
//            if (!file.exists()) {
//                file.mkdirs()
//            }
//            return file.absolutePath + "/" + System.currentTimeMillis() + IMAGE_EXTENSION
//
//        }
    public fun clearData(context: Context) {
        ImageUtils().deleteRecursive(ImageUtils().getMyFolderPath(context ))
    }
    fun deleteRecursive(fileOrDirectory: File) {

        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles()!!)
                deleteRecursive(child)

        fileOrDirectory.delete()

    }

    fun compressImage(context: Context, filePath: String): String {

        /*if (BuildConfig.DEBUG){
            return filePath;
        }*/
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        if (actualHeight==0 || actualWidth==0){
            return filePath
        }

        //      max Height and width values of the compressed image is taken as 816x612

        var maxHeight = 1280.0f
        var maxWidth = 720.0f

        if (options.outHeight != null) {
            maxHeight = options.outHeight.toFloat()
            maxWidth = options.outWidth.toFloat()
        }
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
//            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
//                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
//                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
//                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val fileExtension = getExtension(filePath)
//        val filePathNoExtension=filePath.substring(0,filePath.length-4)
        var filename = filePath.replace(fileExtension, "_Compressed$IMAGE_EXTENSION")
        if (!filePath.startsWith(getMyFolderPath(context).absolutePath, false)) {
            filename =
                createImageFile(context).absolutePath.replace(fileExtension, "_Compressed$IMAGE_EXTENSION")
        }
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 30, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename

    }

    fun getExtension(someFilepath: String): String {
        val extension = someFilepath.substring(someFilepath.lastIndexOf("."))
        return extension
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }
}

