package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.createquest.CreateQuestActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyCreatedFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "RecentlyCreatedFragment";
    private int mPage;
    private FloatingActionButton mFAB;

    public static RecentlyCreatedFragment newInstance(int page) {
        RecentlyCreatedFragment fragment = new RecentlyCreatedFragment();

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
        View view = inflater.inflate(R.layout.fragment_recently_created, container, false);

        mFAB = (FloatingActionButton) view.findViewById(R.id.create_new_quest_fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CreateQuestActivity.start(getContext(), null);
            }
        });
        return view;
    }
}

