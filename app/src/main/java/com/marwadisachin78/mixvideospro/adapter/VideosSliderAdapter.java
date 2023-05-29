package com.marwadisachin78.mixvideospro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.activity.VideosPlayActivity;
import com.marwadisachin78.mixvideospro.model.VideoContentItem;

import java.util.ArrayList;

public class VideosSliderAdapter extends RecyclerView.Adapter<VideosSliderAdapter.SliderViewHolder> {

    ArrayList<VideoContentItem> mSliderArrayList;
    Context mContext;

    public VideosSliderAdapter(Context mContext, ArrayList<VideoContentItem> mSliderArrayList) {
        this.mSliderArrayList = mSliderArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_videos_slider, parent, false);
        return new SliderViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder sliderViewHolder,
                                 @SuppressLint("RecyclerView") final int position) {
        try {
            Glide.with(mContext)
                    .load(mSliderArrayList.get(position).getThumbnail())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder))
                    .into(sliderViewHolder.imgVideosThumb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sliderViewHolder.imgVideosThumb.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideosPlayActivity.class);
            intent.putExtra("link", mSliderArrayList.get(position).getContent());
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return mSliderArrayList.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        FrameLayout layoutFrame;
        RoundedImageView imgVideosThumb;

        public SliderViewHolder(View itemView) {
            super(itemView);

            layoutFrame = itemView.findViewById(R.id.layoutFrame);
            imgVideosThumb = itemView.findViewById(R.id.imgVideosThumb);
        }
    }
}
