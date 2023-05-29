package com.marwadisachin78.mixvideospro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.activity.CategoryVideosWiseActivity;
import com.marwadisachin78.mixvideospro.model.CategoryItem;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    ArrayList<CategoryItem> mCategoryArrayList;
    Context mContext;
    int index = 0;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtCategoryName;
        RoundedImageView imgCategoryThumb;

        public CategoryViewHolder(View view) {
            super(view);
            txtCategoryName = view.findViewById(R.id.txtCategoryName);
            imgCategoryThumb = view.findViewById(R.id.imgCategoryThumb);
        }
    }


    public CategoriesAdapter(Context mContext, ArrayList<CategoryItem> mCategoryArrayList) {
        this.mCategoryArrayList = mCategoryArrayList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        holder.txtCategoryName.setText(mCategoryArrayList.get(position).getName());

        holder.itemView.setOnClickListener(view -> {
            index = position + 3;
            Intent intent = new Intent(mContext, CategoryVideosWiseActivity.class);
            intent.putExtra("position", index);
            intent.putExtra("Name", mCategoryArrayList.get(position).getName());
            mContext.startActivity(intent);
        });

        if (position == 0) {
            holder.imgCategoryThumb.setImageResource(R.drawable.ic_recent);
        } else if (position == 1) {
            holder.imgCategoryThumb.setImageResource(R.drawable.ic_most_popular);
        } else if (position == 2) {
            holder.imgCategoryThumb.setImageResource(R.drawable.ic_top_rated);
        }


    }

    @Override
    public int getItemCount() {
        return mCategoryArrayList.size();
    }

}
