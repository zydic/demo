package com.example.foldergallery.data;

import java.util.List;

import com.example.foldergallery.data.CatDataListVariableModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatDataListModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CatDataListVariableModel> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CatDataListVariableModel> getData() {
        return data;
    }

    public void setData(List<CatDataListVariableModel> data) {
        this.data = data;
    }

}