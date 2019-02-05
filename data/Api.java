package com.example.foldergallery.data;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

   

    public String BASE_URL = "http://h.in/";

    @GET("hh")
    Call<CatDataListModel> getCategoryList();

    @FormUrlEncoded
    @POST("hh")
    Call<SubCategoryDataListModel> getFrameList(@Field("id") int value);

}
