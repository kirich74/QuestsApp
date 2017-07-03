package com.kirich74.questsapp.mainscreen;

/**
 * Created by Kirill Pilipenko on 03.07.2017.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChooseFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};

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
            case 2:
                return RecentlyCreatedFragment.newInstance(2);
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
        return tabTitles[position];
    }
}
