package com.kirich74.questsapp.cloudclient;

import com.kirich74.questsapp.cloudclient.models.AvailableForAll;
import com.kirich74.questsapp.cloudclient.models.AvailableForMe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kirill Pilipenko on 20.10.2017.
 */

public interface ICloudClient {

    @GET("quests_api/index.php")
    Call<List<AvailableForAll>> getAllAvailableQuests(@Query("action") String action);

    @GET("quests_api/index.php")
    Call<List<AvailableForMe>> getAvailableForMeQuests(@Query("action") String action,
            @Query("email") String email);

}
