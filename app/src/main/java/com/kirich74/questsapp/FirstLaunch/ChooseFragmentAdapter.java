package com.kirich74.questsapp.FirstLaunch;

import com.kirich74.questsapp.mainscreen.AvailableQuestsFragment;
import com.kirich74.questsapp.mainscreen.RecentlyCreatedFragment;
import com.kirich74.questsapp.mainscreen.RecentlyPlayedFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class ChooseFragmentAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 1;

        private Context context;

        public ChooseFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case 0:
                    //return OverviewFragment1.newInstance(0);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


    }
