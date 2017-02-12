package com.emobi.convoy.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 23-01-2017.
 */

public class RegisterResultPOJO {
    @SerializedName("message")
    String message;
    @SerializedName("Result")
    RegisterPOJO list_result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegisterPOJO getList_result() {
        return list_result;
    }

    public void setList_result(RegisterPOJO list_result) {
        this.list_result = list_result;
    }

    @Override
    public String toString() {
        return "RegisterResultPOJO{" +
                "message='" + message + '\'' +
                ", list_result=" + list_result +
                '}';
    }
}
