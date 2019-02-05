package com.example.foldergallery.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryDataListVariableModel {

    @SerializedName("ChildCat_Id")
    @Expose
    private Integer childCatId;
    @SerializedName("CatId")
    @Expose
    private String catId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Images")
    @Expose
    private String images;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("UpdatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;

    public Integer getChildCatId() {
        return childCatId;
    }

    public void setChildCatId(Integer childCatId) {
        this.childCatId = childCatId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}