package com.kirich74.questsapp.FirstLaunch;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.createquest.CreateQuestActivity;
import com.kirich74.questsapp.mainscreen.QuestsRecyclerViewAdapter;
import com.kirich74.questsapp.mainscreen.RecentlyCreatedFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class OverviewFragment1 extends android.support.v4.app.Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static OverviewFragment1 newInstance(int page) {
        OverviewFragment1 fragment = new OverviewFragment1();

        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview_1, container, false);

        return view;
    }

}
