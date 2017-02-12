package com.emobi.convoy.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 31-01-2017.
 */

public class NewsPostPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    NewsPostResultPOJO newsPostResultPOJO;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public NewsPostResultPOJO getNewsPostResultPOJO() {
        return newsPostResultPOJO;
    }

    public void setNewsPostResultPOJO(NewsPostResultPOJO newsPostResultPOJO) {
        this.newsPostResultPOJO = newsPostResultPOJO;
    }

    @Override
    public String toString() {
        return "NewsPostPOJO{" +
                "success='" + success + '\'' +
                ", newsPostResultPOJO=" + newsPostResultPOJO +
                '}';
    }
}
