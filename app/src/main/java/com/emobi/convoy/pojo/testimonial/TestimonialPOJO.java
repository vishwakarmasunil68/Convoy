package com.emobi.convoy.pojo.testimonial;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 12-02-2017.
 */

public class TestimonialPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<TestimonialResultPOJO> list_pojo;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<TestimonialResultPOJO> getList_pojo() {
        return list_pojo;
    }

    public void setList_pojo(List<TestimonialResultPOJO> list_pojo) {
        this.list_pojo = list_pojo;
    }

    @Override
    public String toString() {
        return "TestimonialPOJO{" +
                "success='" + success + '\'' +
                ", list_pojo=" + list_pojo +
                '}';
    }
}
