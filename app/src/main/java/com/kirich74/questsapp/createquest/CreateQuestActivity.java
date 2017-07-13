package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateQuestActivity extends MvpActivity
        implements CreateQuestView, android.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "CreateQuestActivity";

    private static final String QUEST = "QUEST";

    private static final int EXISTING_QUEST_LOADER = 0;

    @InjectPresenter
    CreateQuestPresenter mCreateQuestPresenter;

    private Uri mCurrentQuestUri;

    private FloatingActionButton mFAB;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private ItemsRecyclerViewAdapter mAdapter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, CreateQuestActivity.class);

        return intent;
    }



    @Override
    public void showQuestRecyclerView(final Quest quest) {
        mAdapter.setQuest(quest);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveQuest(final ContentValues values, final Uri currentQuestUri) {
        if (currentQuestUri == null) {
            // This is a NEW quest, so insert a new quest into the provider,
            // returning the content URI for the new quest.
            Uri newUri = getContentResolver().insert(QuestEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                //TODO SNACK BAR
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                //TODO
            }
        } else {
            // Otherwise this is an EXISTING quest, so update the quest with content URI
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because currentQuestUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(currentQuestUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                // TODO
            } else {
                // Otherwise, the update was successful and we can display a toast.
                // TODO
            }
        }
        finish();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        String[] projection = {
                QuestEntry._ID,
                QuestEntry.COLUMN_QUEST_NAME,
                QuestEntry.COLUMN_QUEST_AUTHOR,
                QuestEntry.COLUMN_QUEST_DESCRIPTION,
                QuestEntry.COLUMN_QUEST_ACCESS,
                QuestEntry.COLUMN_QUEST_IMAGE,
                QuestEntry.COLUMN_QUEST_DATA_JSON};

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
            String name = cursor.getString(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_NAME));
            String description = cursor
                    .getString(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_DESCRIPTION));
            String dataJson = cursor
                    .getString(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_DATA_JSON));
            String imageUri = cursor
                    .getString(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_IMAGE));
            int access = cursor.getInt(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_ACCESS));

            mCreateQuestPresenter
                    .setQuest(mCurrentQuestUri, name, description, imageUri, dataJson, access);
        }
    }

    @Override
    public void onLoaderReset(final android.content.Loader<Cursor> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mCurrentQuestUri = intent.getData();
            if (mCurrentQuestUri != null) {
                getLoaderManager().initLoader(EXISTING_QUEST_LOADER, null, this);
            }
            else
            {
                mCreateQuestPresenter.CreateEmptyQuest();
            }
        }

        setContentView(R.layout.activity_create_quest);
        mRecyclerView = (RecyclerView) findViewById(R.id.create_quest_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemsRecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCreateQuestPresenter.saveQuest();
            }
        });


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
