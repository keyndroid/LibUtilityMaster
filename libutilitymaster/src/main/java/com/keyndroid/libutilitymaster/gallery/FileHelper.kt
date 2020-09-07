package com.keyndroid.libutilitymaster.gallery

import android.content.Context
import com.developer.cowregistry.lib.gallery.FilePickerActivity
import com.keyndroid.libutilitymaster.gallery.interfaces.CallbackFileSelection
import com.keyndroid.libutilitymaster.utils.EnumHelper

/**
 *Created by Keyur on 20,September,2019
 */
class FileHelper(builder: Builder) {
    var fileTypes: ArrayList<String> = ArrayList()
    var context:Context

    init {
        context=builder.context
        //Convert list of file types into list of string
        for (i in 0..builder.fileTypes.size-1){
            fileTypes.add(builder.fileTypes.get(i).getPicker())
        }
        FilePickerActivity.newIntent(context,fileTypes)
    }

    class Builder(var context: Context) {
        var fileTypes: ArrayList<EnumHelper.FilePicker> = ArrayList()
            private set


        /**
         * Allow user to add single file picker
         */
        fun addFilerPicker(filePicker: EnumHelper.FilePicker): Builder {
            fileTypes.add(filePicker)
            return this
        }

        /**
         * Allow user to add list of file picker
         */
        fun addFilerPicker(filePicker: ArrayList<EnumHelper.FilePicker>): Builder {
            fileTypes.addAll(filePicker)
            return this
        }

        fun addImageMimeType(mimeType:String): Builder {
            FilePickerActivity.IMAGE_MIME_TYPE=mimeType
            return this
        }
        fun addMaxImage(maxImage:Int): Builder {
            FilePickerActivity.IMAGE_MAX_COUNT=maxImage
            return this
        }

        fun addFileMimeType(mimeType:String): Builder {
            FilePickerActivity.FILE_MIME_TYPE=mimeType
            return this
        }
        fun addMaxfile(maxImage:Int): Builder {
            FilePickerActivity.FILE_MAX_COUNT=maxImage
            return this
        }
        fun setCallbackPermission(callbackFilePicker: CallbackFileSelection): Builder {
            FilePickerActivity.callbackFilePicker=callbackFilePicker
            return this
        }

        fun build(): FileHelper {
            if (fileTypes.size == 0) {
                throw IllegalStateException("Please add at least 1 file picker type")
            }
            else if (FilePickerActivity.callbackFilePicker==null){
                throw IllegalStateException("Callback not found")
            }
            return FileHelper(this)
        }
    }
}