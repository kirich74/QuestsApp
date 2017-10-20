package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.CloudClient;
import com.kirich74.questsapp.cloudclient.ICloudClient;
import com.kirich74.questsapp.cloudclient.models.AvailableForMe;
import com.kirich74.questsapp.data.QuestContract;
import com.kirich74.questsapp.playquest.PlayQuestActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kirich74.questsapp.cloudclient.Constants.GET_AVAILABLE_QUESTS;
import static com.kirich74.questsapp.data.QuestContract.QuestEntry._ID;

/**
 * Created by Kirill Pilipenko on 20.10.2017.
 */

public class AvailableQuestsFragment extends android.support.v4.app.Fragment
        implements onQuestActionListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "AvailableQuestsFragment";

    private static final int QUEST_LOADER = 0;

    QuestsRecyclerViewAdapter mCursorAdapter;

    private int mPage;

    private static ICloudClient mICloudClient;

    private RecyclerView questsRecyclerView;

    private Toolbar mToolbar;

    private boolean availableOnlyForMe = true;

    public static AvailableQuestsFragment newInstance(int page) {
        AvailableQuestsFragment fragment = new AvailableQuestsFragment();

        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("page", 0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_quests, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.available_spinner);
        mICloudClient = CloudClient.getApi();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                    final int position, final long id) {
                if (position == 0) {
                    availableOnlyForMe = true;
                } else {
                    availableOnlyForMe = false;
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        ImageButton refresh_button = (ImageButton) view.findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                refresh(availableOnlyForMe);
            }
        });
        questsRecyclerView = (RecyclerView) view.findViewById(R.id.available_quests_recycler_view);
        questsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCursorAdapter = new QuestsRecyclerViewAdapter(this, getContext(), null);
        questsRecyclerView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(QUEST_LOADER, null, this);
        return view;
    }

    public void refresh (boolean isAvailableOnlyForMe){
        if (isAvailableOnlyForMe){
            mICloudClient.getAvailableForMeQuests(GET_AVAILABLE_QUESTS, "kirich74@gmail.com").enqueue(
                    new Callback<List<AvailableForMe>>() {
                        @Override
                        public void onResponse(final Call<List<AvailableForMe>> call,
                                final Response<List<AvailableForMe>> response) {
                            for (AvailableForMe item : response.body()){

                            }
                        }

                        @Override
                        public void onFailure(final Call<List<AvailableForMe>> call, final Throwable t) {

                        }
                    }); //TODO email

        }
    }

    @Override
    public void action(final int id) {
        Intent intent = new Intent(getContext(), PlayQuestActivity.class);
        Uri currentQuestUri = ContentUris.withAppendedId(QuestContract.QuestEntry.CONTENT_URI, id);
        intent.setData(currentQuestUri);
        startActivity(intent);
    }

    @Override
    public String getButtonTitle() {
        return "Play";
    }

    @Override
    public void deleteQuest(final int id) {

        Uri currentQuestUri = ContentUris.withAppendedId(QuestContract.QuestEntry.CONTENT_URI, id);
        if (currentQuestUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContext().getContentResolver().delete(currentQuestUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.

            } else {
                // Otherwise, the delete was successful and we can display a toast.

            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                _ID,
                QuestContract.QuestEntry.COLUMN_QUEST_NAME,
                QuestContract.QuestEntry.COLUMN_QUEST_AUTHOR,
                QuestContract.QuestEntry.COLUMN_QUEST_IMAGE,
                QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION};

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(getContext(),
                // Parent activity context
                QuestContract.QuestEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                _ID + " DESC");                  // Default sort order
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        mCursorAdapter.setCursor(data);
        mCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mCursorAdapter.setCursor(null);
        mCursorAdapter.notifyDataSetChanged();
    }
}
