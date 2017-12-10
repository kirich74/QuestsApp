package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.FirstLaunch.PrefManager;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.createquest.CreateQuestActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirich74.questsapp.data.FileUtils;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;


/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyCreatedFragment extends android.support.v4.app.Fragment
        implements onQuestActionListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "RecentlyCreatedFragment";;

    private int mPage;

    private FloatingActionButton mFAB;

    /** Identifier for the pet data loader */
    private static final int QUEST_LOADER = 0;

    QuestsRecyclerViewAdapter mCursorAdapter;

    private RecyclerView questsRecyclerView;

    private Toolbar mToolbar;

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

        questsRecyclerView = (RecyclerView) view.findViewById(R.id.recently_created_recycler_view);
        questsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCursorAdapter = new QuestsRecyclerViewAdapter(this, getContext(), null);
        questsRecyclerView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(QUEST_LOADER, null, this);
        return view;
    }

    @Override
    public void action(final int id) {
        Intent intent = new Intent(getContext(), CreateQuestActivity.class);
        Uri currentQuestUri = ContentUris.withAppendedId(QuestEntry.CONTENT_URI, id);
        intent.setData(currentQuestUri);
        startActivity(intent);
    }

    public void dataChanged(){
        mCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteQuest(final int id, final int globalId) {

        Uri currentQuestUri = ContentUris.withAppendedId(QuestEntry.CONTENT_URI, id);
        if (currentQuestUri != null) {

            int rowsDeleted = getContext().getContentResolver().delete(currentQuestUri, null, null);
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.

            } else {
                // Otherwise, the delete was successful and we can display a toast.
                FileUtils.deleteDirectory(getContext(),globalId);
            }
        }

    }

    @Override
    public String getButtonTitle() {
        return "EDIT";
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                QuestEntry._ID,
                QuestEntry.COLUMN_QUEST_NAME,
                QuestEntry.COLUMN_QUEST_AUTHOR,
                QuestEntry.COLUMN_QUEST_IMAGE,
                QuestEntry.COLUMN_QUEST_DESCRIPTION,
                QuestEntry.COLUMN_QUEST_GLOBAL_ID};
        PrefManager prefManager = new PrefManager(getActivity());

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(getContext(),   // Parent activity context
                QuestEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                QuestEntry.COLUMN_QUEST_AUTHOR + "=?",                   // No selection clause
                new String[]{prefManager.getSavedEmail()},                  // No selection arguments
                QuestEntry._ID + " DESC");                  // Default sort order
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

