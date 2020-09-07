package com.keyndroid.libutilitymaster.gallery

import android.net.Uri

/**
 * Created by Keyur on 23,September,2019
 */
class FileData{

    //When single image/file is required
    var filePath:String?=null
    var fileUri: Uri?=null
    //When multiple image/file are required
    var listFilePath:MutableList<String> = ArrayList()
    var listUri:MutableList<Uri> = ArrayList()

}
