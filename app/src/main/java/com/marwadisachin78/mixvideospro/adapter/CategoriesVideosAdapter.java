package com.marwadisachin78.mixvideospro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.activity.CategoryVideosWiseActivity;
import com.marwadisachin78.mixvideospro.model.CategoryItem;

import java.util.ArrayList;

public class CategoriesVideosAdapter extends RecyclerView.Adapter<CategoriesVideosAdapter.CategoryVideosViewHolder> {

    ArrayList<CategoryItem> mCategoryArrayList;
    Context mContext;
    VideosContentAdapter mVideosContentAdapter;
    RecyclerView.RecycledViewPool recycledViewPool;

    public class CategoryVideosViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtCategoryName, txtViewAll;
        RecyclerView rvVideosContent;
        LinearLayoutManager linearLayoutManager;

        public CategoryVideosViewHolder(View view) {
            super(view);

            txtCategoryName = view.findViewById(R.id.txtCategoryName);
            txtViewAll = view.findViewById(R.id.txtViewAll);

            rvVideosContent = view.findViewById(R.id.rvVideosContent);
            linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

            rvVideosContent.setHasFixedSize(true);
            rvVideosContent.setNestedScrollingEnabled(false);
            rvVideosContent.setItemAnimator(new DefaultItemAnimator());
            rvVideosContent.setLayoutManager(linearLayoutManager);
        }
    }


    public CategoriesVideosAdapter(Context mContext, ArrayList<CategoryItem> mCategoryArrayList) {
        this.mCategoryArrayList = mCategoryArrayList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public CategoryVideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_videos, parent, false);
        return new CategoryVideosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryVideosViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {

        holder.txtCategoryName.setText(mCategoryArrayList.get(position).getName());

        mVideosContentAdapter = new VideosContentAdapter(mContext,
                mCategoryArrayList.get(position).getContentArrayList(),
                mCategoryArrayList.get(position).getName());
        holder.rvVideosContent.setAdapter(mVideosContentAdapter);
        holder.rvVideosContent.setRecycledViewPool(recycledViewPool);

        holder.txtViewAll.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CategoryVideosWiseActivity.class);
            intent.putExtra("position", position + 1);
            intent.putExtra("Name", mCategoryArrayList.get(position).getName());
            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mCategoryArrayList.size();
    }
}
