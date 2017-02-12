package com.emobi.convoy.pojo.PublicPost;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 24-01-2017.
 */

public class UserLikes {
    @SerializedName("userlikes")
    List<userlikesResponsePOJO> responsePOJO;

    public List<userlikesResponsePOJO> getResponsePOJO() {
        return responsePOJO;
    }

    public void setResponsePOJO(List<userlikesResponsePOJO> responsePOJO) {
        this.responsePOJO = responsePOJO;
    }

    @Override
    public String toString() {
        return "UserLikes{" +
                "responsePOJO=" + responsePOJO +
                '}';
    }
}
