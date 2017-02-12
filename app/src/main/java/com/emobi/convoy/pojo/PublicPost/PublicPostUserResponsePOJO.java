package com.emobi.convoy.pojo.PublicPost;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 24-01-2017.
 */

public class PublicPostUserResponsePOJO {
    @SerializedName("total")
    String total;
    @SerializedName("log_name")
    String log_name;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLog_name() {
        return log_name;
    }

    public void setLog_name(String log_name) {
        this.log_name = log_name;
    }

    @Override
    public String toString() {
        return "PublicPostUserResponsePOJO{" +
                "total='" + total + '\'' +
                ", log_name='" + log_name + '\'' +
                '}';
    }
}
