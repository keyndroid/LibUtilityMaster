package com.libutilitymaster.sample.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.keyndroid.libutilitymaster.gallery.FileData
import com.keyndroid.libutilitymaster.gallery.FileHelper
import com.keyndroid.libutilitymaster.gallery.interfaces.CallbackFileSelection
import com.keyndroid.libutilitymaster.utils.EnumHelper
import com.keyndroid.libutilitymaster.utils.MimeUtils
import com.libutilitymaster.sample.R
import com.libutilitymaster.sample.ui.adapter.MyRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_image_picker.*


/**
 *Created by Keyur on 24,February,2020 12:16 PM
 */
class ImagePickerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)
        button3.setOnClickListener {
            //Run Time permission automatically asked
            val count = etCount.text.toString()
           val fileBuilder = FileHelper.Builder(this)
                .addFilerPicker(EnumHelper.FilePicker.CAMERA)
                .addFilerPicker(EnumHelper.FilePicker.GALLERY)
                .addFilerPicker(EnumHelper.FilePicker.FILE)
                .setCallbackPermission(object : CallbackFileSelection {


                    override fun onFileSelectionFile(fileData: FileData) {
                        if (fileData.listFilePath.size>0 && MimeUtils.getType(fileData.listFilePath.get(0)).contains("image/")){
//                            ivGallery.setImage(fileData.listUri.get(0)/*,R.drawable.ic_launcher_foreground*/)
                            val  mRecyclerView = findViewById(R.id.rvImagePicker) as RecyclerView
                            mRecyclerView.setHasFixedSize(true)
                            val  mLayoutManager = LinearLayoutManager(this@ImagePickerActivity)
                            mRecyclerView.setLayoutManager(mLayoutManager)
                            val listItem = getDataSet(fileData.listFilePath)
                            val mAdapter = MyRecyclerViewAdapter(listItem)
                            mRecyclerView.setAdapter(mAdapter)
                            val snapHelper: SnapHelper = LinearSnapHelper()
                            snapHelper.attachToRecyclerView(mRecyclerView)
                            val itemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecoration(this@ImagePickerActivity, LinearLayoutManager.VERTICAL)
                            mRecyclerView.addItemDecoration(itemDecoration)

                            mAdapter.setOnItemClickListener(object: MyRecyclerViewAdapter.MyClickListener{
                                override fun onItemClick(position: Int, v: View?) {

                                }
                            })
                        }
                    }

                    override fun onFileSelectionError(message: String) {

                    }
                })
            if (!TextUtils.isEmpty(count)){
                val countBalue = count.toInt()
                if (countBalue>=0 && countBalue<=10){
                    fileBuilder.addMaxImage(countBalue)
                }
            }
                fileBuilder
                .build()
        }
    }


    private fun getDataSet(listFilePath: MutableList<String>): ArrayList<String> {
        val results = ArrayList<String>()
        for (index in 0 until listFilePath.size) {
            val obj = listFilePath.get(index)
            results.add(index, obj)
        }
        return results
    }
}