package com.kirich74.questsapp.createquest;

import com.kirich74.questsapp.R;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class CreateQuestActivity extends AppCompatActivity {

    private FloatingActionButton mFAB;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private ItemsRecyclerViewAdapter mAdapter;

    public static void start(final Context context) {
        Intent intent = new Intent(context, CreateQuestActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //------------------
        Quest quest = new Quest();
        quest.createNewQuest("name");
        Item item1 = new Item();
        item1.createTextItem("this is single text");
        quest.addItem(item1.getItem());
        Item item2 = new Item();
        item2.createTextAnswerItem("this is single answer");
        quest.addItem(item2.getItem());
        //------------------

        setContentView(R.layout.activity_create_quest);
        mRecyclerView = (RecyclerView) findViewById(R.id.create_quest_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemsRecyclerViewAdapter(this);
        mAdapter.setQuest(quest);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

}
