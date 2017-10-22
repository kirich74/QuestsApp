package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.CloudClient;
import com.kirich74.questsapp.cloudclient.ICloudClient;
import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.cloudclient.models.DeleteUpdate;
import com.kirich74.questsapp.data.QuestContract;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kirich74.questsapp.cloudclient.Constants.DELETE_ACCESS;
import static com.kirich74.questsapp.cloudclient.Constants.GET_AVAILABLE_QUESTS;
import static com.kirich74.questsapp.cloudclient.Constants.SHOW_MY_QUESTS;

/**
 * Created by Kirill Pilipenko on 20.10.2017.
 */

public class AvailableQuestsFragment extends android.support.v4.app.Fragment
        implements onQuestActionListener {

    public static final String TAG = "AvailableQuestsFragment";

    private static ICloudClient mICloudClient;

    private int mPage;

    private RecyclerView questsRecyclerView;

    private Toolbar mToolbar;

    private AvailableQuestsAdapter mQuestsAdapter;

    private enum sType {
        ForAll,
        ForMe,
        My
    }

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
        final sType[] type = {sType.ForAll};
        mICloudClient = CloudClient.getApi();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                    final int position, final long id) {
                switch (position) {
                    case 0:
                        type[0] = sType.ForAll;
                        break;
                    case 1:
                        type[0] = sType.ForMe;
                        break;
                    case 2:
                        type[0] = sType.My;
                        break;
                }
                refresh(type[0]);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        ImageButton refresh_button = (ImageButton) view.findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                refresh(type[0]);
            }
        });
        questsRecyclerView = (RecyclerView) view.findViewById(R.id.available_quests_recycler_view);
        questsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mQuestsAdapter = new AvailableQuestsAdapter(this, getContext());
        questsRecyclerView.setAdapter(mQuestsAdapter);
        refresh(type[0]);
        return view;
    }

    public void refresh(final sType type) {
        if (type == sType.ForMe) {
            mICloudClient.getAvailableForMeQuests(GET_AVAILABLE_QUESTS,
                    "kirich74@gmail.com") //TODO email
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
                                            .setAvailableQuests(quests, true);
                                }

                                @Override
                                public void onFailure(final Call<List<AvailableQuest>> call,
                                        final Throwable t) {

                                }
                            });
        } else if (type == sType.ForAll) {
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
                                            .setAvailableQuests(quests, false);
                                }

                                @Override
                                public void onFailure(final Call<List<AvailableQuest>> call,
                                        final Throwable t) {

                                }
                            });
        } else {
            mICloudClient.getMyQuests(SHOW_MY_QUESTS,
                    "kirich74@gmail.com") //TODO email
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
                                            .setAvailableQuests(quests, false);
                                }

                                @Override
                                public void onFailure(final Call<List<AvailableQuest>> call,
                                        final Throwable t) {

                                }
                            });
        }
    }

    @Override
    public void action(final int position) {
        AvailableQuest mQuest = mQuestsAdapter.getAvailableQuests().get(position);
        ContentValues values = new ContentValues();
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_NAME, mQuest.getName());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION, mQuest.getDescription());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_AUTHOR, mQuest.getEmail());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_ACCESS, mQuest.getPublic());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_DATA_JSON, mQuest.getDataJson());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_IMAGE, mQuest.getImageUri());
        values.put(QuestContract.QuestEntry.COLUMN_QUEST_GLOBAL_ID, mQuest.getId());
        Uri newUri = getActivity().getContentResolver()
                .insert(QuestContract.QuestEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            //TODO SNACK BAR
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            //TODO
        }
    }

    @Override
    public String getButtonTitle() {
        return "Download";
    }

    @Override
    public void deleteQuest(final int id) {
        mICloudClient.deleteAccess(DELETE_ACCESS, "kirich74@gmail.com", id) //TODO email
                .enqueue(
                        new Callback<DeleteUpdate>() {
                            @Override
                            public void onResponse(final Call<DeleteUpdate> call,
                                    final Response<DeleteUpdate> response) {
                                if (response.body().getResult() == 1) {
                                    Toast.makeText(getContext(), "Access deleted",
                                            Toast.LENGTH_SHORT);
                                    mQuestsAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "Can't delete",
                                            Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(final Call<DeleteUpdate> call,
                                    final Throwable t) {
                                Toast.makeText(getContext(), "Server error",
                                        Toast.LENGTH_SHORT);
                            }
                        });

    }


}
