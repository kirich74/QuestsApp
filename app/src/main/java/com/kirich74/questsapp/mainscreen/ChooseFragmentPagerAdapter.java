package com.kirich74.questsapp.mainscreen;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */


import com.kirich74.questsapp.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChooseFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    private Context context;

    public ChooseFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return RecentlyPlayedFragment.newInstance(0);
            case 1:
                return RecentlyCreatedFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recent played";
            case 1:
                return "Recent created";
            default:
                return null;
        }
    }
}
