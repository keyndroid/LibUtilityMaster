package com.keyndroid.libutilitymaster.gallery.interfaces

import com.keyndroid.libutilitymaster.gallery.FileData


/**
 *Created by Keyur on 23,September,2019
 */
interface CallbackFileSelection {
    fun onFileSelectionFile(fileData: FileData)
    fun onFileSelectionError(message:String)
}