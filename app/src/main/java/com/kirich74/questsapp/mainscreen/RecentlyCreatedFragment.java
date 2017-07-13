package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.createquest.CreateQuestActivity;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kirich74.questsapp.data.QuestContract.QuestEntry;


/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyCreatedFragment extends android.support.v4.app.Fragment
        implements onQuestActionListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "RecentlyCreatedFragment";

    private int mPage;

    private FloatingActionButton mFAB;

    /** Identifier for the pet data loader */
    private static final int QUEST_LOADER = 0;

    QuestsRecyclerViewAdapter mCursorAdapter;

    public static RecentlyCreatedFragment newInstance(int page) {
        RecentlyCreatedFragment fragment = new RecentlyCreatedFragment();

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
        View view = inflater.inflate(R.layout.fragment_recently_created, container, false);

        mFAB = (FloatingActionButton) view.findViewById(R.id.create_new_quest_fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getContext(), CreateQuestActivity.class);
                startActivity(intent);
            }
        });

        ListView questsRecyclerView = (ListView) view.findViewById(R.id.recently_created_recycler_view);
        mCursorAdapter = new QuestsRecyclerViewAdapter(getContext(), null);
        mCursorAdapter.setOnQuestActionListener(this);
        questsRecyclerView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(QUEST_LOADER, null, this);
        return view;
    }

    @Override
    public void startOrEdit(final int id) {
        Intent intent = new Intent(getContext(), CreateQuestActivity.class);
        Uri currentPetUri = ContentUris.withAppendedId(QuestEntry.CONTENT_URI, id);
        intent.setData(currentPetUri);
        startActivity(intent);
    }

    @Override
    public String getButtonTitle() {
        return "EDIT";
    }

    @Override
    public void deleteQuest(final int id) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                QuestEntry._ID,
                QuestEntry.COLUMN_QUEST_NAME,
                QuestEntry.COLUMN_QUEST_AUTHOR,
                QuestEntry.COLUMN_QUEST_DESCRIPTION};

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(getContext(),   // Parent activity context
                QuestEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}

