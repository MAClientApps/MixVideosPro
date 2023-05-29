package com.marwadisachin78.mixvideospro.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.adapter.CategoriesAdapter;
import com.marwadisachin78.mixvideospro.adapter.CategoriesVideosAdapter;
import com.marwadisachin78.mixvideospro.adapter.VideosSliderAdapter;
import com.marwadisachin78.mixvideospro.model.CategoryItem;
import com.marwadisachin78.mixvideospro.model.VideoContentItem;
import com.marwadisachin78.mixvideospro.utility.ScrollZoomLayoutManager;
import com.marwadisachin78.mixvideospro.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator2;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvCategory, rvVideosSlider, rvCategoryVideos;
    String jsonValue = "MixVideosPro.json";
    VideosSliderAdapter mVideosSliderAdapter;
    ScrollZoomLayoutManager mScrollZoomLayoutManager;
    ProgressDialog mProgressDialog;

    ArrayList<CategoryItem> mCategoryArrayList = new ArrayList<>();
    ArrayList<CategoryItem> mSliderVideoArrayList = new ArrayList<>();
    ArrayList<CategoryItem> mCategoryWiseVideoArrayList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager, HorizontalLayout;
    CategoriesVideosAdapter mCategoriesVideosAdapter;
    CategoriesAdapter mCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        rvCategory = findViewById(R.id.rvCategory);
        rvVideosSlider = findViewById(R.id.rvVideosSlider);
        rvCategoryVideos = findViewById(R.id.rvCategoryVideos);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rvCategoryVideos.setLayoutManager(linearLayoutManager);

        if (Utils.isNetworkAvailable(HomeActivity.this)) {
            new getMixVideosPro().execute();
        }

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    public class getMixVideosPro extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog = new ProgressDialog(HomeActivity.this);
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                return Utils.loadJSONFromAsset(HomeActivity.this, jsonValue);
            } catch (Exception e) {
                return null;
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(JSONObject jsonData) {
            super.onPostExecute(jsonData);

            try {
                if (jsonData != null && jsonData.has("Category") &&
                        !jsonData.isNull("Category")) {
                    final JSONArray jsonArrayAction = jsonData.getJSONArray("Category");
                    mSliderVideoArrayList.addAll(CategoryItem.getCategoryList(jsonArrayAction));
                    mCategoryArrayList.addAll(CategoryItem.getCategoryList(jsonArrayAction));
                }

                HorizontalLayout = new LinearLayoutManager(HomeActivity.this,
                        LinearLayoutManager.HORIZONTAL, false);
                rvCategory.setLayoutManager(HorizontalLayout);


                mScrollZoomLayoutManager = new ScrollZoomLayoutManager(HomeActivity.this,
                        Dp2px(30));
                rvVideosSlider.setLayoutManager(mScrollZoomLayoutManager);
                final Handler handler = new Handler();

                handler.postDelayed(() ->
                        mScrollZoomLayoutManager.scrollToPosition(0), 100);


                mVideosSliderAdapter = new VideosSliderAdapter(HomeActivity.this,
                        mSliderVideoArrayList.get(0).getContentArrayList());
                rvVideosSlider.setAdapter(mVideosSliderAdapter);

                PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(rvVideosSlider);

                CircleIndicator2 indicator = findViewById(R.id.videosSliderIndicator);
                indicator.attachToRecyclerView(rvVideosSlider, pagerSnapHelper);

                indicator.animatePageSelected(0);
                // optional
                mVideosSliderAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
                for (int i = 1; i < mSliderVideoArrayList.size(); i++) {
                    CategoryItem categoryItem = new CategoryItem();
                    categoryItem.setID(mSliderVideoArrayList.get(i).getID());
                    categoryItem.setIcon(mSliderVideoArrayList.get(i).getIcon());
                    categoryItem.setName(mSliderVideoArrayList.get(i).getName());
                    ArrayList<VideoContentItem> videoContentItemArrayListSort = new ArrayList<>();
                    for (int j = 0; j < 5; j++) {
                        videoContentItemArrayListSort.add(mSliderVideoArrayList.get(i).getContentArrayList().get(j));
                    }
                    categoryItem.setContentArrayList(videoContentItemArrayListSort);
                    mCategoryWiseVideoArrayList.add(categoryItem);
                }

                mCategoryWiseVideoArrayList.remove(2);
                mCategoryWiseVideoArrayList.remove(2);
                mCategoryWiseVideoArrayList.remove(2);
                mCategoriesVideosAdapter = new CategoriesVideosAdapter(HomeActivity.this, mCategoryWiseVideoArrayList);
                rvCategoryVideos.setAdapter(mCategoriesVideosAdapter);
                mCategoriesVideosAdapter.notifyDataSetChanged();

                mCategoryArrayList.remove(0);
                mCategoryArrayList.remove(0);
                mCategoryArrayList.remove(0);
                mCategoriesAdapter = new CategoriesAdapter(HomeActivity.this,
                        mCategoryArrayList);
                rvCategory.setAdapter(mCategoriesAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public int Dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}