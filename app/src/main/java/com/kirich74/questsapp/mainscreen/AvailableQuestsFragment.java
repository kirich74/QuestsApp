package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.CloudClient;
import com.kirich74.questsapp.cloudclient.ICloudClient;
import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.cloudclient.models.Delete;
import com.kirich74.questsapp.data.QuestContract;
import com.kirich74.questsapp.playquest.PlayQuestActivity;

import android.content.ContentUris;
import android.content.Intent;
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

    private boolean availableOnlyForMe = true;

    private AvailableQuestsAdapter mQuestsAdapter;

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
                if (position == 0) {
                    availableOnlyForMe = true;
                } else {
                    availableOnlyForMe = false;
                }
                refresh(availableOnlyForMe);
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
        mQuestsAdapter = new AvailableQuestsAdapter(this, getContext());
        questsRecyclerView.setAdapter(mQuestsAdapter);
        refresh(availableOnlyForMe);
        return view;
    }

    public void refresh(final boolean isAvailableOnlyForMe) {
        if (isAvailableOnlyForMe) {
            mICloudClient.getAvailableForMeQuests(GET_AVAILABLE_QUESTS,
                    "kirich74@gmail.com") //TODO email
                    .enqueue(
                            new Callback<List<AvailableQuest>>() {
                                @Override
                                public void onResponse(final Call<List<AvailableQuest>> call,
                                        final Response<List<AvailableQuest>> response) {
                                    List<AvailableQuest> quests = new ArrayList<AvailableQuest>();
                                    for (AvailableQuest item : response.body()) {
                                        quests.add(item);
                                    }
                                    mQuestsAdapter.setAvailableQuests(quests, isAvailableOnlyForMe);
                                }

                                @Override
                                public void onFailure(final Call<List<AvailableQuest>> call,
                                        final Throwable t) {

                                }
                            });
        } else {
            mICloudClient.getAllAvailableQuests(GET_AVAILABLE_QUESTS)
                    .enqueue(
                            new Callback<List<AvailableQuest>>() {
                                @Override
                                public void onResponse(final Call<List<AvailableQuest>> call,
                                        final Response<List<AvailableQuest>> response) {
                                    List<AvailableQuest> quests = new ArrayList<AvailableQuest>();
                                    for (AvailableQuest item : response.body()) {
                                        quests.add(item);
                                    }
                                    mQuestsAdapter.setAvailableQuests(quests, isAvailableOnlyForMe);
                                }

                                @Override
                                public void onFailure(final Call<List<AvailableQuest>> call,
                                        final Throwable t) {

                                }
                            });
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
        return "Download";
    }

    @Override
    public void deleteQuest(final int id) {
        mICloudClient.deleteAccess(DELETE_ACCESS, "kirich74@gmail.com", id) //TODO email
                .enqueue(
                        new Callback<Delete>() {
                            @Override
                            public void onResponse(final Call<Delete> call,
                                    final Response<Delete> response) {
                                if (response.body().getResult() == 1) {
                                    Toast.makeText(getContext(), "Access deleted",
                                            Toast.LENGTH_SHORT);
                                    mQuestsAdapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(getContext(), "Can't delete",
                                            Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(final Call<Delete> call,
                                    final Throwable t) {
                                Toast.makeText(getContext(), "Server error",
                                        Toast.LENGTH_SHORT);
                            }
                        });

    }


}
