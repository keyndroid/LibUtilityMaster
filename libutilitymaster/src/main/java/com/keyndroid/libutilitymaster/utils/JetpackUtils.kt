package com.keyndroid.libutilitymaster.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File


/**
 * Created by Keyur on 01,October,2019 2:36 PM
 */
object JetpackUtils{
    fun getShareIntent(imageUris:ArrayList<Uri>): Intent {
//        val imageUris = ArrayList<Uri>()


        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        shareIntent.type = "image/*"
        //startActivity(Intent.createChooser(shareIntent, "Share images to.."))
        return shareIntent

    }

    fun getShareIntent(sharingData:String): Intent {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingData)
        sendIntent.type = "text/plain"

        val shareIntent = Intent.createChooser(sendIntent, null)
       // startActivity(shareIntent)
        return shareIntent
    }

    fun startDownload(file:File,context: Context) {
//        val file = File(getExternalFilesDir(null), "Dummy")

        val request =
            DownloadManager.Request(Uri.parse("http://speedtest.ftp.otenet.gr/files/test10Mb.db"))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
//                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

    }
}
