package com.emobi.convoy.pojo.PublicPost;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 24-01-2017.
 */

public class userlikesResponsePOJO {
    @SerializedName("like_id")
    String like_id;
    @SerializedName("like_user_id")
    String like_user_id;
    @SerializedName("like_post_id")
    String like_post_id;
    @SerializedName("like_status")
    String like_status;

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getLike_user_id() {
        return like_user_id;
    }

    public void setLike_user_id(String like_user_id) {
        this.like_user_id = like_user_id;
    }

    public String getLike_post_id() {
        return like_post_id;
    }

    public void setLike_post_id(String like_post_id) {
        this.like_post_id = like_post_id;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    @Override
    public String toString() {
        return "userlikesResponsePOJO{" +
                "like_id='" + like_id + '\'' +
                ", like_user_id='" + like_user_id + '\'' +
                ", like_post_id='" + like_post_id + '\'' +
                ", like_status='" + like_status + '\'' +
                '}';
    }
}
