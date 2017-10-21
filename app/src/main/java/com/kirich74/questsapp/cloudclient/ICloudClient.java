package com.kirich74.questsapp.cloudclient;

import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.cloudclient.models.Delete;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<Delete> deleteAccess(@Query("action") String action,
            @Query("email") String email, @Query("id") int id);

}
