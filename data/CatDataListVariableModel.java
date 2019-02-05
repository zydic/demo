package com.example.foldergallery.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatDataListVariableModel {

    private int Category_Id;

    private String Name;

    private String ThumbImg;

    private String NoOfImages;

    private String Status;

    private String CreatedBy;
    private String CreatedAt;
    private String UpdatedBy;

    public String getNoOfImages() {
        return NoOfImages;
    }

    public void setNoOfImages(String noOfImages) {
        NoOfImages = noOfImages;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    private String UpdatedAt;

    private int isStatic;

    private int isdownloaded;


    public int getCategory_Id() {
        return Category_Id;
    }

    public void setCategory_Id(int category_Id) {
        Category_Id = category_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getThumbImg() {
        return ThumbImg;
    }

    public void setThumbImg(String thumbImg) {
        ThumbImg = thumbImg;
    }

    public int getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(int isStatic) {
        this.isStatic = isStatic;
    }

    public int getIsdownloaded() {
        return isdownloaded;
    }

    public void setIsdownloaded(int isdownloaded) {
        this.isdownloaded = isdownloaded;
    }


}
