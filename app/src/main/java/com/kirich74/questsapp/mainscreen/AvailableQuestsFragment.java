package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.FirstLaunch.PrefManager;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.CloudClient;
import com.kirich74.questsapp.cloudclient.ICloudClient;
import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.cloudclient.models.DeleteUpdate;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kirich74.questsapp.cloudclient.Constants.DELETE;
import static com.kirich74.questsapp.cloudclient.Constants.DELETE_ACCESS;
import static com.kirich74.questsapp.cloudclient.Constants.GET_AVAILABLE_QUESTS;
import static com.kirich74.questsapp.cloudclient.Constants.SHOW_MY_QUESTS;
import static com.kirich74.questsapp.data.ItemType.AVAILABLE_FOR_ALL;
import static com.kirich74.questsapp.data.ItemType.AVAILABLE_FOR_ME;
import static com.kirich74.questsapp.data.ItemType.MY_QUESTS;

/**
 * Created by Kirill Pilipenko on 20.10.2017.
 */

public class AvailableQuestsFragment extends android.support.v4.app.Fragment
        implements onAvailableQuestActionListener {

    public static final String TAG = "AvailableQuestsFragment";

    private static ICloudClient mICloudClient;

    private int mPage;

    private RecyclerView questsRecyclerView;

    private Toolbar mToolbar;

    private AvailableQuestsAdapter mQuestsAdapter;

    private int spinnerPosition = AVAILABLE_FOR_ALL;

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                    final int position, final long id) {
                switch (position) {
                    case 0:
                        spinnerPosition = AVAILABLE_FOR_ALL;
                        break;
                    case 1:
                        spinnerPosition = AVAILABLE_FOR_ME;
                        break;
                    case 2:
                        spinnerPosition = MY_QUESTS;
                        break;
                }
                refresh(spinnerPosition);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        ImageButton refresh_button = (ImageButton) view.findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                refresh(spinnerPosition);
            }
        });
        questsRecyclerView = (RecyclerView) view.findViewById(R.id.available_quests_recycler_view);
        questsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mQuestsAdapter = new AvailableQuestsAdapter(this, getContext());
        questsRecyclerView.setAdapter(mQuestsAdapter);
        refresh(spinnerPosition);
        return view;
    }

    public void refresh(final int spinnerPosition) {
        PrefManager prefManager = new PrefManager(getActivity());
        switch (spinnerPosition) {
            case AVAILABLE_FOR_ALL:
                mICloudClient.getAllAvailableQuests(GET_AVAILABLE_QUESTS)
                        .enqueue(
                                new Callback<List<AvailableQuest>>() {
                                    @Override
                                    public void onResponse(final Call<List<AvailableQuest>> call,
                                            final Response<List<AvailableQuest>> response) {
                                        List<AvailableQuest> quests
                                                = new ArrayList<AvailableQuest>();
                                        if (response.body() != null) {
                                            for (AvailableQuest item : response.body()) {
                                                quests.add(item);
                                            }
                                        }
                                        mQuestsAdapter
                                                .setAvailableQuests(quests, spinnerPosition);
                                    }

                                    @Override
                                    public void onFailure(final Call<List<AvailableQuest>> call,
                                            final Throwable t) {

                                    }
                                });
                break;
            case AVAILABLE_FOR_ME:
                mICloudClient.getAvailableForMeQuests(GET_AVAILABLE_QUESTS,
                        prefManager.getSavedEmail())
                        .enqueue(
                                new Callback<List<AvailableQuest>>() {
                                    @Override
                                    public void onResponse(final Call<List<AvailableQuest>> call,
                                            final Response<List<AvailableQuest>> response) {
                                        List<AvailableQuest> quests
                                                = new ArrayList<AvailableQuest>();
                                        if (response.body() != null) {
                                            for (AvailableQuest item : response.body()) {
                                                quests.add(item);
                                            }
                                        }
                                        mQuestsAdapter
                                                .setAvailableQuests(quests, spinnerPosition);
                                    }

                                    @Override
                                    public void onFailure(final Call<List<AvailableQuest>> call,
                                            final Throwable t) {

                                    }
                                });
                break;
            case MY_QUESTS:
                mICloudClient.getMyQuests(SHOW_MY_QUESTS,
                        prefManager.getSavedEmail())
                        .enqueue(
                                new Callback<List<AvailableQuest>>() {
                                    @Override
                                    public void onResponse(final Call<List<AvailableQuest>> call,
                                            final Response<List<AvailableQuest>> response) {
                                        List<AvailableQuest> quests
                                                = new ArrayList<AvailableQuest>();
                                        if (response.body() != null) {
                                            for (AvailableQuest item : response.body()) {
                                                quests.add(item);
                                            }
                                        }
                                        mQuestsAdapter
                                                .setAvailableQuests(quests, spinnerPosition);
                                    }

                                    @Override
                                    public void onFailure(final Call<List<AvailableQuest>> call,
                                            final Throwable t) {
                                        Toast.makeText(getActivity(), "Connection error",
                                                Toast.LENGTH_SHORT);
                                    }
                                });
                break;
        }
    }

    public void download(@NonNull final String fileName, final int globalId){
        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
                return;
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

                try {
                    File storageDir = getContext()
                            .getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + globalId);
                    File file = new File(storageDir, fileName);
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    ostream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                return;
            }
        };

        Picasso.with(getContext())
                .load("http://kirich74.h1n.ru/quests_api/upload" + fileName)
                .into(target);
    }


    @Override
    public void download(final int position) {
        AvailableQuest mQuest = mQuestsAdapter.getAvailableQuests().get(position);
        ContentValues values = new ContentValues();
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_NAME, mQuest.getName());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION, mQuest.getDescription());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_AUTHOR, mQuest.getEmail());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_ACCESS, mQuest.getPublic());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_DATA_JSON, mQuest.getDataJson());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_IMAGE, mQuest.getImageUri());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_GLOBAL_ID, mQuest.getId());
        String[] projection = {
                QuestContract.QuestEntry._ID};

        // This loader will execute the ContentProvider's query method on a background thread
        Cursor cursor = getActivity().getContentResolver().query(
                QuestContract.QuestEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                QuestContract.QuestEntry.COLUMN_QUEST_GLOBAL_ID + "=?",
                // No selection clause
                new String[]{String.valueOf(mQuest.getId())},
                // No selection arguments
                null);                  // Default sort order

        if (cursor == null || cursor.getCount() == 0) {
            Uri newUri = getActivity().getContentResolver()
                    .insert(QuestContract.QuestEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(getActivity(), "Error with insertion", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(getActivity(), "Quest downloaded", Toast.LENGTH_SHORT).show();
            }
        } else {
            getActivity().getContentResolver().update(QuestContract.QuestEntry.CONTENT_URI, values,
                    QuestContract.QuestEntry.COLUMN_QUEST_GLOBAL_ID + "=?",
                    new String[]{String.valueOf(mQuest.getId())});
            Toast.makeText(getActivity(), "Quest updated", Toast.LENGTH_SHORT).show();
        }

        download(mQuest.getImageUri().substring(mQuest.getImageUri().lastIndexOf("/")), mQuest.getId());
        Quest downloadedQuest = new Quest("", "", "", 0, mQuest.getDataJson(), 0);
        for (int i = 1; i <= downloadedQuest.quest.size(); i++) {
            if (downloadedQuest.getImageUri(i) != null)
                download(downloadedQuest.getImageUri(i).toString().substring(mQuest.getImageUri().lastIndexOf("/")),mQuest.getId());
        }
    }

    @Override
    public String getButtonTitle(int id) {
        return getString(R.string.download);
    }

    @Override
    public void deleteQuest(final int id) {
        mICloudClient.delete(DELETE, id)
                .enqueue(
                        new Callback<DeleteUpdate>() {
                            @Override
                            public void onResponse(final Call<DeleteUpdate> call,
                                    final Response<DeleteUpdate> response) {
                                if (response.body().getResult() == 1) {
                                    Toast.makeText(getActivity(), "Deleted",
                                            Toast.LENGTH_SHORT);
                                    refresh(spinnerPosition);
                                } else {
                                    Toast.makeText(getActivity(), "Can't delete",
                                            Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(final Call<DeleteUpdate> call,
                                    final Throwable t) {
                                Toast.makeText(getActivity(), "Server error",
                                        Toast.LENGTH_SHORT);
                            }
                        });

    }

    @Override
    public void deleteAccess(final int id) {
        PrefManager prefManager = new PrefManager(getActivity());
        mICloudClient.deleteAccess(DELETE_ACCESS, prefManager.getSavedEmail(), id)
                .enqueue(
                        new Callback<DeleteUpdate>() {
                            @Override
                            public void onResponse(final Call<DeleteUpdate> call,
                                    final Response<DeleteUpdate> response) {
                                if (response.body().getResult() == 1) {
                                    Toast.makeText(getActivity(), "Access deleted",
                                            Toast.LENGTH_SHORT);
                                    refresh(spinnerPosition);
                                } else {
                                    Toast.makeText(getActivity(), "Can't delete",
                                            Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(final Call<DeleteUpdate> call,
                                    final Throwable t) {
                                Toast.makeText(getActivity(), "Server error",
                                        Toast.LENGTH_SHORT);
                            }
                        });

    }


}
