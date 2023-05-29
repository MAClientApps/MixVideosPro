package com.marwadisachin78.mixvideospro.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.adapter.CategoryVideosWiseAdapter;
import com.marwadisachin78.mixvideospro.model.CategoryItem;
import com.marwadisachin78.mixvideospro.model.VideoContentItem;
import com.marwadisachin78.mixvideospro.utility.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryVideosWiseActivity extends AppCompatActivity {

    RecyclerView rvCategoryVideosWise;
    String jsonValue = "MixVideosPro.json";
    ProgressDialog mProgressDialog;
    ArrayList<CategoryItem> mCategoryWiseVideoArrayList = new ArrayList<>();
    ArrayList<VideoContentItem> mContentVideoArrayList = new ArrayList<>();
    GridLayoutManager mGridLayoutManager;
    CategoryVideosWiseAdapter mCategoryVideosWiseAdapter;
    int position;
    String strName = "";
    Toolbar toolbar;
    TextView txtToolbarTitle;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_videos_wise);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strName = null;
                position = 0;
            } else {
                position = getIntent().getIntExtra("position", 0);
                strName = extras.getString("Name");
            }
        } else {
            position = (Integer) savedInstanceState.getSerializable("position");
            strName = (String) savedInstanceState.getSerializable("Name");
        }

        toolbar = findViewById(R.id.toolbar);
        txtToolbarTitle = toolbar.findViewById(R.id.txtToolbarTitle);
        imgBack = toolbar.findViewById(R.id.imgBack);
        txtToolbarTitle.setText(strName);

        imgBack.setOnClickListener(view -> onBackPressed());

        mGridLayoutManager = new GridLayoutManager(CategoryVideosWiseActivity.this,
                2);

        rvCategoryVideosWise = findViewById(R.id.rvCategoryVideosWise);
        rvCategoryVideosWise.setLayoutManager(mGridLayoutManager);

        if (Utils.isNetworkAvailable(CategoryVideosWiseActivity.this)) {
            new GetCategoryWiseVideos().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    public class GetCategoryWiseVideos extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog = new ProgressDialog(CategoryVideosWiseActivity.this);
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                return Utils.loadJSONFromAsset(CategoryVideosWiseActivity.this, jsonValue);
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(JSONObject jsonData) {
            super.onPostExecute(jsonData);


            try {
                if (jsonData != null && jsonData.has("Category") &&
                        !jsonData.isNull("Category")) {
                    final JSONArray jsonArrayAction = jsonData.getJSONArray("Category");
                    mCategoryWiseVideoArrayList.addAll(CategoryItem.getCategoryList(jsonArrayAction));
                }

                mContentVideoArrayList.addAll(mCategoryWiseVideoArrayList.get(position).getContentArrayList());
                mCategoryVideosWiseAdapter = new CategoryVideosWiseAdapter(CategoryVideosWiseActivity.this, mContentVideoArrayList, strName);
                rvCategoryVideosWise.setAdapter(mCategoryVideosWiseAdapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}