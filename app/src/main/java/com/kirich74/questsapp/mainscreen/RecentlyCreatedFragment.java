package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */

public class RecentlyCreatedFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "RecentlyCreatedFragment";
    private int mPage;

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
        return inflater.inflate(R.layout.fragment_recently_created, container, false);
    }
}

