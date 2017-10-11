package com.kirich74.questsapp.playquest;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

public class PlayQuestActivity extends MvpActivity
        implements android.app.LoaderManager.LoaderCallbacks<Cursor>, PlayQuestView,
        onItemActionListener {

    public static final String TAG = "PlayQuestActivity";

    static final int GALLERY_REQUEST = 1;

    private static final String QUEST = "QUEST";

    private static final int EXISTING_QUEST_LOADER = 0;

    @InjectPresenter
    PlayQuestPresenter mPlayQuestPresenter;

    private Uri mCurrentQuestUri;

    private FloatingActionButton mFAB;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private ItemsRecyclerViewAdapter mAdapter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, PlayQuestActivity.class);

        return intent;
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        String[] projection = {
                QuestContract.QuestEntry._ID,
                QuestContract.QuestEntry.COLUMN_QUEST_NAME,
                QuestContract.QuestEntry.COLUMN_QUEST_AUTHOR,
                QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION,
                QuestContract.QuestEntry.COLUMN_QUEST_ACCESS,
                QuestContract.QuestEntry.COLUMN_QUEST_IMAGE,
                QuestContract.QuestEntry.COLUMN_QUEST_DATA_JSON};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentQuestUri,         // Query the content URI for the current quest
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(final android.content.Loader<Cursor> loader, final Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of quest attributes that we're interested in

            // Extract out the value from the Cursor for the given column index
            String name = cursor
                    .getString(cursor.getColumnIndex(QuestContract.QuestEntry.COLUMN_QUEST_NAME));
            String description = cursor
                    .getString(cursor.getColumnIndex(
                            QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION));
            String dataJson = cursor
                    .getString(
                            cursor.getColumnIndex(QuestContract.QuestEntry.COLUMN_QUEST_DATA_JSON));
            String imageUri = cursor
                    .getString(cursor.getColumnIndex(QuestContract.QuestEntry.COLUMN_QUEST_IMAGE));
            int access = cursor
                    .getInt(cursor.getColumnIndex(QuestContract.QuestEntry.COLUMN_QUEST_ACCESS));

            mPlayQuestPresenter
                    .setQuest(mCurrentQuestUri, name, description, imageUri, dataJson, access);
        }
    }

    @Override
    public void onLoaderReset(final android.content.Loader<Cursor> loader) {

    }

    @Override
    public void showStepRecyclerView(final Quest quest, final int begin, final int end) {
        mAdapter.setQuest(quest);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void questCompleted() {
        Toast.makeText(this, "Quest successfully completed", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void wrongAnswerToast() {
        Toast.makeText(this, "Wrong answer", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mCurrentQuestUri = intent.getData();
            if (mCurrentQuestUri != null) {
                getLoaderManager().initLoader(EXISTING_QUEST_LOADER, null, this);
            } else {
                finish();
            }
        }

        setContentView(R.layout.activity_play_quest);
        mRecyclerView = (RecyclerView) findViewById(R.id.play_quest_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemsRecyclerViewAdapter(this, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        if (isFinishing()) {
            getMvpDelegate().onDestroy();
        }
    }
}
