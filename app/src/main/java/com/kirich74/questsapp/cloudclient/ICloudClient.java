package com.kirich74.questsapp.cloudclient;

import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.cloudclient.models.DeleteUpdate;
import com.kirich74.questsapp.cloudclient.models.Insert;
import com.kirich74.questsapp.cloudclient.models.Send;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Kirill Pilipenko on 20.10.2017.
 */

public interface ICloudClient {

    @GET("quests_api/index.php")
    Call<List<AvailableQuest>> getAllAvailableQuests(@Query("action") String action);

    @GET("quests_api/index.php")
    Call<List<AvailableQuest>> getAvailableForMeQuests(@Query("action") String action,
            @Query("email") String email);

    @GET("quests_api/index.php")
    Call<List<AvailableQuest>> getMyQuests(@Query("action") String action,
            @Query("email") String email);

    @GET("quests_api/index.php")
    Call<DeleteUpdate> deleteAccess(@Query("action") String action,
            @Query("email") String email, @Query("id") int id);

    @GET("quests_api/index.php")
    Call<List<Send>> sendAccess(@Query("action") String action,
            @Query("email") String email, @Query("id") int id);

    @GET("quests_api/index.php")
    Call<List<Insert>> insert(@Query("action") String action,
            @Query("email") String email, @Query("name") String name,
            @Query("description") String description, @Query("image_uri") String image_uri,
            @Query("data_json") String data_json, @Query("public") int access);

    @GET("quests_api/index.php")
    Call<DeleteUpdate> delete(@Query("action") String action,
            @Query("id") int id);

    @GET("quests_api/index.php")
    Call<DeleteUpdate> update(@Query("action") String action,
            @Query("email") String email, @Query("name") String name, @Query("id") int id,
            @Query("description") String description, @Query("image_uri") String image_uri,
            @Query("data_json") String data_json, @Query("public") int access);

    /*@Multipart
    @POST("upload.php")
    Call<Result> uploadImage(@Part MultipartBody.Part file);*/
}
