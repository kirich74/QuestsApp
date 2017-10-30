package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.createquest.CreateQuestActivity;
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

import static com.kirich74.questsapp.data.QuestContract.QuestEntry._ID;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyPlayedFragment extends android.support.v4.app.Fragment
        implements onQuestActionListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "RecentlyPlayedFragment";

    private static final int QUEST_LOADER = 0;

    QuestsRecyclerViewAdapter mCursorAdapter;

    private int mPage;

    private RecyclerView questsRecyclerView;

    private Toolbar mToolbar;

    public static RecentlyPlayedFragment newInstance(int page) {
        RecentlyPlayedFragment fragment = new RecentlyPlayedFragment();

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
        View view = inflater.inflate(R.layout.fragment_recently_played, container, false);
        questsRecyclerView = (RecyclerView) view.findViewById(R.id.recently_played_recycler_view);
        questsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCursorAdapter = new QuestsRecyclerViewAdapter(this, getContext(), null);
        questsRecyclerView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(QUEST_LOADER, null, this);
        return view;
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

