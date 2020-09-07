package com.keyndroid.libutilitymaster.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.keyndroid.libutilitymaster.R
import com.keyndroid.libutilitymaster.dialogs.MaterialDialogs
import com.keyndroid.libutilitymaster.utils.LibUtil
import com.keyndroid.libutilitymaster.utils.PERMISSION_LIST
import com.keyndroid.libutilitymaster.utils.PERMISSION_REQUEST


internal class PermissionActivity : AppCompatActivity() {

    // This variable is for Sending user to settings screen, if he/she wish
    private  var isFromSettings=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermission()
    }

    override fun onResume() {
        super.onResume()
        if (isFromSettings){
            askPermission()
        }
    }
    private fun askPermission() {
        val permissionList: Array<String> = intent.getStringArrayExtra(PERMISSION_LIST)!!

        ActivityCompat.requestPermissions(
            this,
            permissionList,
            PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST -> {


                var showRationale = true
                var isAllPermissionGranted = false
                for (permissionCheck in 0..grantResults.size - 1) {
                    if (grantResults[permissionCheck] != PackageManager.PERMISSION_GRANTED) {
                        break
                    }
                    if (permissionCheck == grantResults.size - 1) {
                        isAllPermissionGranted = true
                    }
                }
                for (permissionCheck in 0..permissions.size - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale= shouldShowRequestPermissionRationale(permissions.get(permissionCheck))
                    } else {
                        showRationale = false
                    }


                }
                if (!isAllPermissionGranted && !showRationale) {
                    //User has denied with never ask again
                    if (isFromSettings){
                        //If user is already comes from setting screen then this block will be executed
                        callbackPermission?.onPermissionDenied()
                        resetCallback()
                        return
                    }
                    //Ask user to open settings and retry permission
                    MaterialDialogs(null)
                        .setCallback(object : MaterialDialogs.DialogCallback {
                            override fun onNegativeClick() {
                                callbackPermission?.onPermissionDenied()
                                resetCallback()
                            }

                            override fun onPositiveClick() {
                                isFromSettings=true
//                                val intent = Intent()
//                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                val uri = Uri.fromParts("package", packageName, null)
//                                intent.data = uri
//                                startActivity(intent)
                                LibUtil.startInstalledAppDetailsActivity(this@PermissionActivity)
                            }
                        })
                        .create(
                        this,
                        getString(R.string.permission_never_ask_again_message),
                            getString(R.string.open_Settings),
                            getString(R.string.lbl_cancel)
                    )
                        .setCancelable(false)
                       .show()
                    //return
                }
                else if (!isAllPermissionGranted){
                    callbackPermission?.onPermissionDenied()
                    resetCallback()
                }
                else{
                    //All Permission granted
                    callbackPermission?.onPermissionGranted()
                    resetCallback()
                }
                return
            }
        }

    }

    private fun resetCallback() {
        callbackPermission=null
        finish()
    }

    companion object {
        var callbackPermission: CallbackPermission? = null


        fun newIntent(context: Context, permisssionList: Array<String>) {
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(PERMISSION_LIST, permisssionList)
            context.startActivity(intent)
//            return intent
        }
    }
}