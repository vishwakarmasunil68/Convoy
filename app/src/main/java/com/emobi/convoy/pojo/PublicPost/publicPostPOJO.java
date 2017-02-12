package com.emobi.convoy.pojo.PublicPost;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 24-01-2017.
 */

public class publicPostPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<PublicPostResponsePOJO> list_news;
    @SerializedName("results")
    List<PublicPostUserResponsePOJO> list_user_names;
    @SerializedName("Success")
    String like_success;
//    @SerializedName("userlikes")
//    UserLikes userLikes;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<PublicPostResponsePOJO> getList_news() {
        return list_news;
    }

    public void setList_news(List<PublicPostResponsePOJO> list_news) {
        this.list_news = list_news;
    }

    public List<PublicPostUserResponsePOJO> getList_user_names() {
        return list_user_names;
    }

    public void setList_user_names(List<PublicPostUserResponsePOJO> list_user_names) {
        this.list_user_names = list_user_names;
    }

    public String getLike_success() {
        return like_success;
    }

    public void setLike_success(String like_success) {
        this.like_success = like_success;
    }

//    public UserLikes getUserLikes() {
//        return userLikes;
//    }
//
//    public void setUserLikes(UserLikes userLikes) {
//        this.userLikes = userLikes;
//    }

    @Override
    public String toString() {
        return "publicPostPOJO{" +
                "success='" + success + '\'' +
                ", list_news=" + list_news +
                ", list_user_names=" + list_user_names +
                ", like_success='" + like_success + '\'' +
//                ", userLikes=" + userLikes +
                '}';
    }
}
