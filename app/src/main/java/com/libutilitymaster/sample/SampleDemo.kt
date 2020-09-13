package com.libutilitymaster.sample

import android.content.Context
import android.widget.Toast
import com.keyndroid.libutilitymaster.gallery.FileData
import com.keyndroid.libutilitymaster.gallery.FileHelper
import com.keyndroid.libutilitymaster.gallery.interfaces.CallbackFileSelection
import com.keyndroid.libutilitymaster.utils.EnumHelper
import com.keyndroid.libutilitymaster.utils.MimeUtils

/**
 *Created by Keyur on 18,February,2020 3:59 PM
 */
fun openFilePicker(context: Context){
    FileHelper.Builder(context)
        .addFilerPicker(EnumHelper.FilePicker.CAMERA)
        .addFilerPicker(EnumHelper.FilePicker.GALLERY)
        .addFilerPicker(EnumHelper.FilePicker.FILE)
        .addMaxImage(5)
        .addMaxfile(6)
        .addFileMimeType(MimeUtils.getType(".pdf"))
        .setCallbackPermission(object : CallbackFileSelection {

            override fun onFileSelectionFile(fileData: FileData) {
                if (fileData.listFilePath.size>0 && MimeUtils.getType(fileData.listFilePath.get(0)).contains("image/")){
                    Toast.makeText(context,"Total File Selected = "+fileData.listFilePath.size,Toast.LENGTH_SHORT).show()
//                    ivGallery.setImage(fileData.listUri.get(0)/*,R.drawable.ic_launcher_foreground*/)
                }
            }

            override fun onFileSelectionError(message: String) {
                Toast.makeText(context,"File Selection Error= "+message,Toast.LENGTH_SHORT).show()

            }
        }).build()
}