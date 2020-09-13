package com.libutilitymaster.sample.ui.activity

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keyndroid.libutilitymaster.location.BoundLocationManager
import com.keyndroid.libutilitymaster.permission.CallbackPermission
import com.keyndroid.libutilitymaster.permission.PermissionUtils
import com.keyndroid.libutilitymaster.utils.CommonUtils
import com.keyndroid.libutilitymaster.utils.EnumHelper
import com.keyndroid.libutilitymaster.utils.FOLDER_NAME
import com.keyndroid.libutilitymaster.utils.NotificationHelper
import com.libutilitymaster.sample.R
import com.libutilitymaster.sample.openFilePicker
import com.libutilitymaster.sample.ui.adapter.SampleAdapter
import java.io.File

/**
 * Created by Keyur on 01,October,2019 4:20 PM
 */
class SampleActivity : AppCompatActivity() {
    var pos =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val  mRecyclerView = findViewById(R.id.recyclerview) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        val  mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.setReverseLayout(true)
        mRecyclerView.setLayoutManager(mLayoutManager)
        val listItem = getDataSet()
        val mAdapter =
            SampleAdapter(listItem)

        mRecyclerView.setAdapter(mAdapter)
        mRecyclerView.smoothScrollToPosition(listItem.size - 1)

        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        mAdapter.setOnItemClickListener(object: SampleAdapter.MyClickListener{
            override fun onItemClick(position: Int, v: View?) {
                when(position){
                    7-> {
                        val notificatioNnHelper = NotificationHelper(this@SampleActivity)
                        val id = pos % 6
                        val intent = Intent(this@SampleActivity, SampleActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        notificatioNnHelper.createNOtification(true,id, intent,
                            R.drawable.ic_launcher_foreground
                        )
                        pos++
                    }
                    6 -> {
                        showLocationUpdate()
                    }
                    5 -> {
                        startActivity(Intent(this@SampleActivity,
                            ImagePickerActivity::class.java))
                    }
                    4 -> {
                        showdDialog()
                    }
                    3 -> {
                        val intent = CommonUtils.getShareIntent("Its Temporary Data for sharing purpose")
                        startActivity(intent)
                    }
                    2 -> {
                        //Store file at the place of Android/data/package-name-of-application/files/
                        val file = //File(getExternalFilesDir(null), "Dummy")
                            getExternalFilesDir(FOLDER_NAME)!!
                        CommonUtils.startDownload(file,this@SampleActivity)
                    }
                    1 -> {
                        openFilePicker(this@SampleActivity)
                    }
                    0 -> {
                        startActivity(Intent(this@SampleActivity, GalleryActivity::class.java))
                    }
                    else -> { Toast.makeText(this@SampleActivity,"Its inappropriate",Toast.LENGTH_SHORT).show()}
                }
            }
        })
    }


    private fun getDataSet(): ArrayList<String> {
        val results = ArrayList<String>()
        results.add("Gallery")//0
        results.add("Single File Picker")//1
        results.add("Download Manager")//2
        results.add("Text Sharing")//3
        results.add("Permission")//4
        results.add("Custom File Picker")//5
        results.add("Location Update")//6
        results.add("Notification Helper")//7
        return results
    }
    private fun showdDialog() {
        PermissionUtils.Builder().setContenxt(this)
            .addPermission(EnumHelper.Permission.STORAGE)
            .setCallbackPermission(object : CallbackPermission {
                override fun onPermissionDenied() {
                    Log.e("PermissionMyCheck","PermissionMyCheck==Get Lost")
                    Toast.makeText(this@SampleActivity,"Denied Permission",Toast.LENGTH_SHORT).show()
//                    finish()
                }

                override fun onPermissionGranted() {
                    Log.e("PermissionMyCheck","PermissionMyCheck==Granted")
                    Toast.makeText(this@SampleActivity,"Granted Permission",Toast.LENGTH_SHORT).show()
                }
            })
            .build()
    }
    private fun showLocationUpdate() {

        BoundLocationManager.bindLocationListenerIn(this@SampleActivity,object :
            LocationListener{

            override fun onLocationChanged(p0: Location) {

            }

            override fun onStatusChanged(
                provider: String?,
                status: Int,
                extras: Bundle?
            ) {

            }

        },applicationContext)
    }
}
