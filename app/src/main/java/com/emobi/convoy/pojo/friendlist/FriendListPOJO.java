package com.emobi.convoy.pojo.friendlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 24-01-2017.
 */

public class FriendListPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<FriendListResponsePOJO> list_friends;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<FriendListResponsePOJO> getList_friends() {
        return list_friends;
    }

    public void setList_friends(List<FriendListResponsePOJO> list_friends) {
        this.list_friends = list_friends;
    }

    @Override
    public String toString() {
        return "FriendListPOJO{" +
                "success='" + success + '\'' +
                ", list_friends=" + list_friends +
                '}';
    }
}
