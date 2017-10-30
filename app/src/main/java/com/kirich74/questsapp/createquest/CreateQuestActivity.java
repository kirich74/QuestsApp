package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kirich74.questsapp.FirstLaunch.PrefManager;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.CloudClient;
import com.kirich74.questsapp.cloudclient.ICloudClient;
import com.kirich74.questsapp.cloudclient.models.DeleteUpdate;
import com.kirich74.questsapp.cloudclient.models.Insert;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kirich74.questsapp.cloudclient.Constants.INSERT;
import static com.kirich74.questsapp.cloudclient.Constants.UPDATE;

public class CreateQuestActivity extends MvpActivity
        implements CreateQuestView, android.app.LoaderManager.LoaderCallbacks<Cursor>,
        onItemActionListener {

    public static final String TAG = "CreateQuestActivity";

    static final int GALLERY_REQUEST = 1;

    private static final String QUEST = "QUEST";

    private static final int EXISTING_QUEST_LOADER = 0;

    private static ICloudClient mICloudClient;

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
    public void onSaveQuest(final Quest mQuest, final Uri currentQuestUri) {
        PrefManager prefManager = new PrefManager(this);
        final ContentValues values = new ContentValues();
        values.put(QuestEntry.COLUMN_QUEST_NAME, mQuest.mName);
        values.put(QuestEntry.COLUMN_QUEST_DESCRIPTION, mQuest.mDescription);
        values.put(QuestEntry.COLUMN_QUEST_AUTHOR, prefManager.getSavedEmail());
        values.put(QuestEntry.COLUMN_QUEST_ACCESS, mQuest.mAccess);
        values.put(QuestEntry.COLUMN_QUEST_DATA_JSON, mQuest.getQuestJsonArrayString());
        values.put(QuestEntry.COLUMN_QUEST_IMAGE, mQuest.mMainImageUri);
        if (currentQuestUri == null) {
            // This is a NEW quest, so insert a new quest into the provider,
            // returning the content URI for the new quest.
            final Uri newUri = getContentResolver().insert(QuestEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Can't save quest", Toast.LENGTH_SHORT).show();
            } else {
                mICloudClient = CloudClient.getApi();
                mICloudClient.insert(INSERT,
                        prefManager.getSavedEmail(), mQuest.mName, mQuest.mDescription,
                        mQuest.mMainImageUri, mQuest.getQuestJsonArrayString(),
                        mQuest.mAccess)
                        .enqueue(
                                new Callback<List<Insert>>() {
                                    @Override
                                    public void onResponse(final Call<List<Insert>> call,
                                            final Response<List<Insert>> response) {
                                        if (response.body() != null) {
                                            values.put(QuestEntry.COLUMN_QUEST_GLOBAL_ID,
                                                    response.body().get(0).getIdentity());
                                            int rowsAffected = getContentResolver()
                                                    .update(newUri, values, null, null);

                                            // Show a toast message depending on whether or not the update was successful.
                                            if (rowsAffected == 0) {
                                                // If no rows were affected, then there was an error with the update.
                                                Toast.makeText(getApplication(), "Can't save quest",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(final Call<List<Insert>> call,
                                            final Throwable t) {
                                        finish();
                                    }
                                });
            }
        } else {
            // Otherwise this is an EXISTING quest, so update the quest with content URI
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because currentQuestUri will already identify the correct row in the database that
            // we want to modify.
            values.put(QuestEntry.COLUMN_QUEST_GLOBAL_ID, mQuest.getGlobalId());
            int rowsAffected = getContentResolver().update(currentQuestUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(getApplication(), "Can't save quest", Toast.LENGTH_SHORT).show();
            } else {
                mICloudClient = CloudClient.getApi();
                mICloudClient.update(UPDATE,
                        prefManager.getSavedEmail(), mQuest.mName, mQuest.mGlobalId, mQuest.mDescription,
                        mQuest.mMainImageUri, mQuest.getQuestJsonArrayString(),
                        mQuest.mAccess)
                        .enqueue(
                                new Callback<DeleteUpdate>() {
                                    @Override
                                    public void onResponse(final Call<DeleteUpdate> call,
                                            final Response<DeleteUpdate> response) {
                                        if (response.body() != null
                                                && response.body().getResult() == 1) {
                                            Toast.makeText(getApplication(), "Successful",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(final Call<DeleteUpdate> call,
                                            final Throwable t) {
                                        Toast.makeText(getApplication(), "Server Error",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
            }
        }
        finish();
    }

    @Override
    public void showQuestRecyclerView(
            final Quest quest) {
        mAdapter.setQuest(quest);
        mRecyclerView.setAdapter(mAdapter);
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
                QuestEntry.COLUMN_QUEST_DATA_JSON,
                QuestEntry.COLUMN_QUEST_GLOBAL_ID};

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
            int globalId = cursor.getInt(cursor.getColumnIndex(QuestEntry.COLUMN_QUEST_GLOBAL_ID));

            mCreateQuestPresenter
                    .setQuest(mCurrentQuestUri, name, description, imageUri, dataJson, access,
                            globalId);
        }
    }

    @Override
    public void onLoaderReset(final android.content.Loader<Cursor> loader) {

    }

    @Override
    public void setImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mAdapter.saveImage(bitmap);
                }
        }
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
                mCreateQuestPresenter.CreateEmptyQuest();
            }
        }

        setContentView(R.layout.activity_create_quest);
        mRecyclerView = (RecyclerView) findViewById(R.id.create_quest_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemsRecyclerViewAdapter(this, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

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
