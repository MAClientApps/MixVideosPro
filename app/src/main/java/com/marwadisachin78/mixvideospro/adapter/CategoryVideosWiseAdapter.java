package com.marwadisachin78.mixvideospro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.activity.VideosPlayActivity;
import com.marwadisachin78.mixvideospro.model.VideoContentItem;

import java.util.ArrayList;

public class CategoryVideosWiseAdapter extends RecyclerView.Adapter<CategoryVideosWiseAdapter.CategoryWiseVideoViewHolder> {

    ArrayList<VideoContentItem> mCategoryWiseVideoArrayList;
    Context mContext;
    String strCategoryName;


    public static class CategoryWiseVideoViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgVideosThumb;

        public CategoryWiseVideoViewHolder(View view) {
            super(view);
            imgVideosThumb = view.findViewById(R.id.imgVideosThumb);
        }
    }


    public CategoryVideosWiseAdapter(Context mContext, ArrayList<VideoContentItem> mCategoryWiseVideoArrayList,
                                     final String cname) {
        this.mCategoryWiseVideoArrayList = mCategoryWiseVideoArrayList;
        this.mContext = mContext;
        this.strCategoryName = cname;

    }

    @NonNull
    @Override
    public CategoryWiseVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_videos_wise, parent, false);
        return new CategoryWiseVideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryWiseVideoViewHolder categoryWiseVideoViewHolder,
                                 @SuppressLint("RecyclerView") final int position) {

        try {

            try {
                final String imageUrl = mCategoryWiseVideoArrayList.get(position).getThumbnail();

                Glide.with(mContext)
                        .load(new GlideUrl(imageUrl))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_placeholder)
                                .error(R.drawable.ic_placeholder))
                        .into(categoryWiseVideoViewHolder.imgVideosThumb);

            } catch (Exception e) {
                e.printStackTrace();
            }
            categoryWiseVideoViewHolder.imgVideosThumb.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, VideosPlayActivity.class);
                intent.putExtra("link", mCategoryWiseVideoArrayList.get(position).getContent());
                mContext.startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryWiseVideoArrayList.size();
    }

}
