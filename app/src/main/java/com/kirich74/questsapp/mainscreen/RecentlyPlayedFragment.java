package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyPlayedFragment extends android.support.v4.app.Fragment implements onQuestActionListener {

    public static final String TAG = "RecentlyPlayedFragment";
    private int mPage;

    public static RecentlyPlayedFragment newInstance(int page) {
        RecentlyPlayedFragment fragment = new RecentlyPlayedFragment();

        Bundle args = new Bundle();
        args.putInt("page",page);
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
        return inflater.inflate(R.layout.fragment_recently_played, container, false);
    }


    @Override
    public void startOrEdit(final int id) {

    }

    @Override
    public String getButtonTitle() {
        return null;
    }

    @Override
    public void deleteQuest(final int id) {

    }
}

