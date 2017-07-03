package com.kirich74.questsapp.ui;

import com.kirich74.questsapp.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class CreateQuestActivity extends AppCompatActivity {
    public static void start(final Context context) {
        Intent intent = new Intent(context, CreateQuestActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
    }

}
