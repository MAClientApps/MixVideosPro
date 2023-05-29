package com.marwadisachin78.mixvideospro.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryItem {

    String Name, Icon;
    ArrayList<VideoContentItem> mContentArrayList;
    int ID;

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<VideoContentItem> getContentArrayList() {
        return mContentArrayList;
    }

    public void setContentArrayList(ArrayList<VideoContentItem> videoContentItemArrayList) {
        this.mContentArrayList = videoContentItemArrayList;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static ArrayList<CategoryItem> getCategoryList(JSONArray jsonArray) {
        ArrayList<CategoryItem> categoryItemArrayList = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    categoryItemArrayList.add(getCategoryFromJson(jObj));
                }
            }
            return categoryItemArrayList;
        } catch (Exception e) {
            return categoryItemArrayList;
        }
    }

    private static CategoryItem getCategoryFromJson(JSONObject jObj) {
        CategoryItem categoryItem = new CategoryItem();
        try {

            if (jObj.has("ID") && !jObj.isNull("ID"))
                categoryItem.setID(jObj.getInt("ID"));

            if (jObj.has("Name") && !jObj.isNull("Name"))
                categoryItem.setName(jObj.getString("Name"));

            if (jObj.has("Icon") && !jObj.isNull("Icon"))
                categoryItem.setIcon(jObj.getString("Icon"));

            if (jObj.has("Content") && !jObj.isNull("Content"))
                categoryItem.setContentArrayList(VideoContentItem.getContentList(jObj.getJSONArray("Content")));


            return categoryItem;
        } catch (JSONException e) {
            return categoryItem;
        }
    }
}
