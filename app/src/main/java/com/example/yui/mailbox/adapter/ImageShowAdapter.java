package com.example.yui.mailbox.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yui.mailbox.R;

import com.bumptech.glide.Glide;

public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.ViewHolder> {

    private Context mContext;
    private int[] imageList ;

    public ImageShowAdapter(int[] list){
        this.imageList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        ImageView mImageView;

        public ViewHolder(View view){
            super(view);
            mCardView = (CardView)view;
            mImageView = (ImageView)view.findViewById(R.id.item_img);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_iamge_show_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int imageId = imageList[position];
        Glide.with(mContext).load(imageId).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return imageList.length;
    }
}
