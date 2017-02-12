package com.emobi.convoy.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 31-01-2017.
 */

public class NewsPostResultPOJO {
    @SerializedName("post_id")
    String post_id;
    @SerializedName("post_user_id")
    String post_user_id;
    @SerializedName("post_cat_id")
    String post_cat_id;
    @SerializedName("post_msg")
    String post_msg;
    @SerializedName("post_image")
    String post_image;
    @SerializedName("post_time")
    String post_time;
    @SerializedName("post_date")
    String post_date;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_cat_id() {
        return post_cat_id;
    }

    public void setPost_cat_id(String post_cat_id) {
        this.post_cat_id = post_cat_id;
    }

    public String getPost_msg() {
        return post_msg;
    }

    public void setPost_msg(String post_msg) {
        this.post_msg = post_msg;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    @Override
    public String toString() {
        return "NewsPostResultPOJO{" +
                "post_id='" + post_id + '\'' +
                ", post_user_id='" + post_user_id + '\'' +
                ", post_cat_id='" + post_cat_id + '\'' +
                ", post_msg='" + post_msg + '\'' +
                ", post_image='" + post_image + '\'' +
                ", post_time='" + post_time + '\'' +
                ", post_date='" + post_date + '\'' +
                '}';
    }
}
