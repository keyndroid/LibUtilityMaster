package com.libutilitymaster.sample.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.libutilitymaster.sample.R
import com.libutilitymaster.sample.ui.adapter.MyRecyclerViewAdapter

class GalleryActivity : AppCompatActivity() {
    val NEW_WORD_ACTIVITY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

      val  mRecyclerView = findViewById(R.id.recyclerview) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
       val  mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.setLayoutManager(mLayoutManager)
        val listItem = getDataSet()
        val mAdapter = MyRecyclerViewAdapter(listItem)
        mRecyclerView.setAdapter(mAdapter)
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecoration)

        mAdapter.setOnItemClickListener(object: MyRecyclerViewAdapter.MyClickListener{
            override fun onItemClick(position: Int, v: View?) {

            }
        })

    }

    private fun getDataSet(): ArrayList<String> {
        val results = ArrayList<String>()
        val list=resources.getStringArray(R.array.list_images)
        for (index in 0 until list.size) {
            val obj = list.get(index)
            results.add(index, obj)
        }
        return results
    }
}
