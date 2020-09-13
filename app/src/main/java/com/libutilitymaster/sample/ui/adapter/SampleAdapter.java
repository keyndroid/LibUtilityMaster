package com.libutilitymaster.sample.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.keyndroid.libutilitymaster.views.CustomImageView;
import com.libutilitymaster.sample.R;

import java.util.ArrayList;

public class SampleAdapter extends RecyclerView
        .Adapter<SampleAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<String> mDataset;
    private static MyClickListener myClickListener;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView tvTitle;
        CustomImageView profile2;
        public DataObjectHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            profile2 = itemView.findViewById(R.id.profile2);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), profile2);
        }
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public SampleAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_2, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String fileName=  mDataset.get(position);
        int startLengeth = fileName.lastIndexOf("/")+1;
        int length=fileName.length();
        fileName=    fileName.substring(startLengeth ,length);

        holder.tvTitle.setText(fileName);


//        holder.profile.setVisibility(View.GONE);
//        holder.profile2.setVisibility(View.GONE);
//        Glide.with(holder.itemView.getContext())
//                .asBitmap()
//                .load(mDataset.get(position))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.profile);
//
//        holder.profile2.setImage(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}