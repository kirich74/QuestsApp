package com.kirich74.questsapp.createquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.arellomobile.mvp.MvpActivity;

import com.kirich74.questsapp.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class CreateQuestActivity extends MvpActivity implements CreateQuestView {

    public static final String TAG = "CreateQuestActivity";

    private static final String QUEST = "QUEST";

    @InjectPresenter
    CreateQuestPresenter mCreateQuestPresenter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, CreateQuestActivity.class);

        return intent;
    }
    private FloatingActionButton mFAB;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private ItemsRecyclerViewAdapter mAdapter;

    public static void start(final Context context, String quest) {
        Intent intent = new Intent(context, CreateQuestActivity.class);
        intent.putExtra(QUEST, quest);
        context.startActivity(intent);
    }

    @Override
    public void showQuestRecyclerView(final Quest quest) {
        mAdapter.setQuest(quest);
    }
@Override
protected void onDestroy(){
    Log.d(TAG, "onDestroy");
    super.onDestroy();

    if (isFinishing()) {
        getMvpDelegate().onDestroy();
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String questString = intent.getStringExtra(QUEST);
            mCreateQuestPresenter.setQuest(questString);
        }

        setContentView(R.layout.activity_create_quest);
        mRecyclerView = (RecyclerView) findViewById(R.id.create_quest_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemsRecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }
}
