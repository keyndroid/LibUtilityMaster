package com.developer.cowregistry.lib.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.keyndroid.libutilitymaster.R
import com.keyndroid.libutilitymaster.dialogs.MaterialDialogs
import com.keyndroid.libutilitymaster.gallery.FileData
import com.keyndroid.libutilitymaster.gallery.FileUtils
import com.keyndroid.libutilitymaster.gallery.interfaces.CallbackFileSelection
import com.keyndroid.libutilitymaster.permission.CallbackPermission
import com.keyndroid.libutilitymaster.permission.PermissionUtils
import com.keyndroid.libutilitymaster.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Keyur on 20,September,2019
 * Carefully handle this activity because you have to finish it in every case
 */
open class FilePickerActivity : AppCompatActivity() {
    lateinit var outPutfileUri: Uri
    lateinit var imageFilePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listFileType = intent.getStringArrayListExtra(PERMISSION_LIST)

        askPermission(listFileType!!)
    }

    lateinit var dialog: MaterialDialogs
    private fun askPermission(listFileType: ArrayList<String>) {
        if (listFileType.size > 1) {
            dialog = MaterialDialogs(EnumHelper.STYLE.Filled)
                .setCallback(object : MaterialDialogs.DialogCallback {
                    override fun onNegativeClick() {

                        resetCallback()
                    }

                    override fun onPositiveClick() {
                        openPicker(listFileType.get(dialog.selectedItem))
                    }
                })
            dialog.create(
                this,
                getString(R.string.ttl_file_picker_option),
                listFileType.toTypedArray(),
                getString(R.string.lbl_ok),
                getString(R.string.lbl_cancel),
                0
            ).setCancelable(false)
                .show()
        } else {
            openPicker(listFileType.get(0))
        }
    }

    private fun openPicker
//                (listFileType: ArrayList<String>)
                (fileType: String) {
        var func = operationGallery()
        var permissionBuilder = PermissionUtils.Builder().setContenxt(this)
//        for (fileType in listFileType) {
        when (fileType) {
            EnumHelper.FilePicker.CAMERA.getPicker() -> {
                func = operationCamera()
                permissionBuilder.addPermission(
                    EnumHelper.Permission.CAMERA,
                    EnumHelper.Permission.STORAGE
                )
            }
            EnumHelper.FilePicker.FILE.getPicker(), EnumHelper.FilePicker.GALLERY.getPicker() -> {
                if (EnumHelper.FilePicker.FILE.getPicker() == fileType) {
                    func = operationFilePicker()
                }
                permissionBuilder.addPermission(EnumHelper.Permission.STORAGE)
            }
        }
//        }

        permissionBuilder.setCallbackPermission(object : CallbackPermission {
            override fun onPermissionDenied() {
                //Log.e("PermissionMyCheck","PermissionMyCheck==Get Lost")
                finish()
            }

            override fun onPermissionGranted() {
                func()
                LoggerUtils.e("PermissionMyCheck==Granted")
            }
        })
            .build()
    }

    fun operationGallery(): () -> Unit {                                     // 1
        return ::openGallery
    }

    fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "image/*"
            if (IMAGE_MIME_TYPE != null) {
                type = IMAGE_MIME_TYPE
            }

        }
        if (IMAGE_MAX_COUNT > 1) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

//        val mimetypes = arrayOf("image/*", "video/*")
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)

        // Start the Intent
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    fun operationFilePicker(): () -> Unit {                                     // 1
        return ::openFilePicker
    }

    fun openFilePicker() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "*/*"
            if (FILE_MIME_TYPE != null) {
                type = FILE_MIME_TYPE
            }

        }
        if (FILE_MAX_COUNT > 1) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        // Start the Intent
        startActivityForResult(intent, FILE_REQUEST)
    }

    fun operationCamera(): () -> Unit {                                     // 1
        return ::openCamera
    }

    fun openCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val file = File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg")
        val file2 = FileUtils().createImageFile(this)
        imageFilePath = file2.absolutePath
        outPutfileUri = FileProvider.getUriForFile(
            this,
            getApplicationContext().getPackageName() + ".provider",
            file2
        );
//        Uri.fromFile(file2)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri)
        startActivityForResult(captureIntent, CAMERA_REQUEST)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        imageFilePath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    //pic coming from camera
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(),
                            outPutfileUri
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
//                    GlobalScope.launch{
//
//                    }
                    val fileData = FileData()
                    fileData.listFilePath.add(imageFilePath)
                    fileData.listUri.add(outPutfileUri)
                    compressImage(fileData, false)
//                    ImageUtils().compressImage(imageFilePath)
//                    val fileData = FileData()
//                    fileData.filePath=imageFilePath
//                    fileData.fileUri=outPutfileUri
//                    callbackFilePicker!!.onFileSelectionFile(fileData)

                } else {
                    resetCallback()
                }
            }

            GALLERY_REQUEST, FILE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    //pick image from gallery
                    val selectedImage = data?.getData()
                    val fileData = FileData()

                    if (selectedImage == null) {
                        val clipData = data?.clipData
                        var maxCount = clipData!!.itemCount-1
                        //Handle if user has selected more than maximum count
                        if (requestCode==FILE_REQUEST && maxCount> FILE_MAX_COUNT){
                            maxCount=FILE_MAX_COUNT
                        }
                        else if(requestCode== GALLERY_REQUEST && maxCount> IMAGE_MAX_COUNT){
                            maxCount=IMAGE_MAX_COUNT
                        }
                        for (i in 0..maxCount) {
                            val uri = clipData.getItemAt(i).uri
                            val path = RealPathUtil.getRealPath(this, uri)
                            if (TextUtils.isEmpty(path)){
                                continue
                            }
                            fileData.listFilePath.add(path)
                            fileData.listUri.add(uri)
                        }
                        if (fileData.listUri.size==0){
                            callbackFilePicker!!.onFileSelectionError(getString(R.string.err_image))
                            return
                        }

                        compressImage(fileData, requestCode==FILE_REQUEST)
                    } else {
                        val path = RealPathUtil.getRealPath(this, selectedImage)
                        if (TextUtils.isEmpty(path)){
                            callbackFilePicker!!.onFileSelectionError(getString(R.string.err_image))
                            return
                        }
                        imageFilePath = path
                        outPutfileUri = selectedImage
//                        GlobalScope.launch{
//
//                        }
                        fileData.listFilePath.add(path)
                        fileData.listUri.add(selectedImage)
                        compressImage(fileData, false)
                    }

//                    ImageUtils().compressImage(path)
//                    val fileData = FileData()
//                    fileData.filePath=path
//                    fileData.fileUri=selectedImage
//                    callbackFilePicker!!.onFileSelectionFile(fileData)

//                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//
//                    // Get the cursor
//                    val cursor = getContentResolver()
//                        .query(selectedImage!!, filePathColumn, null, null, null)
//                    // Move to first row
//                    cursor!!.moveToFirst()
//
//                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                    val imgDecodableString = cursor.getString(columnIndex)
//                    cursor?.close()
//                    val bitmap = BitmapFactory.decodeFile(imgDecodableString)


                } else {
                    resetCallback()
                }
            }
        }

    }

    fun compressImage(fileData: FileData, isFileType: Boolean) = runBlocking {
        val job = GlobalScope.launch {
            // launch a new coroutine and keep a reference to its Job

            for (i in 0..fileData.listFilePath.size - 1) {
                val filePath = fileData.listFilePath.get(i)
                if (MimeUtils.getType(filePath).contains("image/", true)) {
                    val imagePathCompressed = FileUtils().compressImage(this@FilePickerActivity,filePath)
//                    val compressedImageFile = Compressor.compress(this@FilePickerActivity, File(filePath)){
//                        default()
//                        destination(ImageUtils().createImageFile())
//                    }
//                    var imagePathCompressed=compressedImageFile.absolutePath

//                    runOnUiThread {
//                        Toast.makeText(this@FilePickerActivity,"Image C Path="+imagePathCompressed,Toast.LENGTH_LONG).show()
//                    }
                    val compressedUri = FileProvider.getUriForFile(
                        this@FilePickerActivity,
                        getApplicationContext().getPackageName() + ".provider",
                        File(imagePathCompressed)
                    )

                    if ((isFileType && FILE_MAX_COUNT > 1) || (!isFileType && IMAGE_MAX_COUNT > 1)) {
                        fileData.listFilePath.set(i, imagePathCompressed)
                        fileData.listUri.set(i, compressedUri)
                    } else {
                        fileData.filePath = imagePathCompressed
                        fileData.fileUri = compressedUri
                    }
                    //Uri.fromFile(File(imagePathCompressed))
                }
            }



            println("World!")
        }
        println("Hello,")
        job.join() // wait until child coroutine completes
        callbackFilePicker!!.onFileSelectionFile(fileData)
        resetCallback()
    }
//    fun compressImage(){
//        val fileData = FileData()
////        val job =  GlobalScope.launch
//        coroutineScope{ // launch a new coroutine in background and continue
//            launch(Dispatchers.Default){
//                val imagePathCompressed = ImageUtils().compressImage(imageFilePath)
//
//                if (::imageFilePath.isInitialized && ::outPutfileUri.isInitialized){
//                    fileData.filePath=imagePathCompressed
//                    fileData.fileUri=Uri.parse(imagePathCompressed)
//                }
//                else{
//
//                }
//            }
//        }
//        //job.join()
//        callbackFilePicker!!.onFileSelectionFile(fileData)
//        main()
//    }


    override fun onDestroy() {
        super.onDestroy()
        //clearData();
    }

    private fun clearData() {
        if (::imageFilePath.isInitialized) {
            FileUtils().deleteRecursive(FileUtils().getMyFolderPath(this))
        }
    }

    private fun resetCallback() {
        callbackFilePicker = null
        IMAGE_MIME_TYPE = null
        FILE_MIME_TYPE = null
        IMAGE_MAX_COUNT = 1
        FILE_MAX_COUNT = 1
        finish()
    }

    companion object {
        var callbackFilePicker: CallbackFileSelection? = null
        var IMAGE_MIME_TYPE: String? = null
        var FILE_MIME_TYPE: String? = null
        var IMAGE_MAX_COUNT: Int = 1
        var FILE_MAX_COUNT: Int = 1

        fun newIntent(context: Context, filePicker: ArrayList<String>) {
            val intent = Intent(context, FilePickerActivity::class.java)
            intent.putStringArrayListExtra(PERMISSION_LIST, filePicker)
            context.startActivity(intent)
//            return intent
        }
    }
}
