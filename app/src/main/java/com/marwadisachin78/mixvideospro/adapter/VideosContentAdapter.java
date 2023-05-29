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

public class VideosContentAdapter extends RecyclerView.Adapter<VideosContentAdapter.ContentViewHolder> {

    ArrayList<VideoContentItem> mContentArrayList;
    Context mContext;
    String strCategoryName;

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgVideosThumb;

        public ContentViewHolder(View view) {
            super(view);
            imgVideosThumb = view.findViewById(R.id.imgVideosThumb);
        }
    }


    public VideosContentAdapter(Context mContext, ArrayList<VideoContentItem> mContentArrayList,
                                final String cname) {
        this.mContentArrayList = mContentArrayList;
        this.mContext = mContext;
        this.strCategoryName = cname;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_videos_content, parent, false);
        return new ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContentViewHolder contentViewHolder,
                                 @SuppressLint("RecyclerView") final int position) {

        try {

            try {
                final String imageUrl = mContentArrayList.get(position).getThumbnail();

                Glide.with(mContext)
                        .load(new GlideUrl(imageUrl))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_placeholder)
                                .error(R.drawable.ic_placeholder))
                        .into(contentViewHolder.imgVideosThumb);

            } catch (Exception e) {
                e.printStackTrace();
            }

            contentViewHolder.imgVideosThumb.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, VideosPlayActivity.class);
                intent.putExtra("link", mContentArrayList.get(position).getContent());
                mContext.startActivity(intent);
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mContentArrayList.size();
    }

}
