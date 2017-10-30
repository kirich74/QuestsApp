package com.kirich74.questsapp.FirstLaunch;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.mainscreen.ChooseFragmentPagerAdapter;
import com.kirich74.questsapp.mainscreen.MainActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class FirstLaunchActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private MenuItem deleteMenuItem;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.first_launch_view_pager);
        viewPager.setAdapter(new ChooseFragmentAdapter(getSupportFragmentManager(),
                FirstLaunchActivity.this));
    }
}
