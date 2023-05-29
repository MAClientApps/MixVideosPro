package com.marwadisachin78.mixvideospro.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoContentItem {

    String Title, Thumbnail, Content;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public static ArrayList<VideoContentItem> getContentList(JSONArray jsonArray) {

        ArrayList<VideoContentItem> videoContentItemArrayList = new ArrayList<>();
        try {

            if (jsonArray != null && jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);

                    videoContentItemArrayList.add(getContentFromJson(jObj));

                }
            }
            return videoContentItemArrayList;
        } catch (Exception e) {
            return videoContentItemArrayList;
        }
    }

    private static VideoContentItem getContentFromJson(JSONObject jObj) {
        VideoContentItem videoContentItem = new VideoContentItem();
        try {
            if (jObj.has("Title") && !jObj.isNull("Title"))
                videoContentItem.setTitle(jObj.getString("Title"));

            if (jObj.has("Thumbnail") && !jObj.isNull("Thumbnail"))
                videoContentItem.setThumbnail(jObj.getString("Thumbnail"));

            if (jObj.has("Thumbnail_Large") && !jObj.isNull("Thumbnail_Large"))
                videoContentItem.setThumbnail(jObj.getString("Thumbnail_Large"));

            if (jObj.has("Content") && !jObj.isNull("Content"))
                videoContentItem.setContent(jObj.getString("Content"));

            return videoContentItem;
        } catch (JSONException e) {
            return videoContentItem;
        }
    }
}
