package com.kirich74.questsapp.FirstLaunch;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.QuestContract;
import com.kirich74.questsapp.mainscreen.ChooseFragmentPagerAdapter;
import com.kirich74.questsapp.mainscreen.MainActivity;
import com.kirich74.questsapp.mainscreen.RecentlyCreatedFragment;
import com.kirich74.questsapp.playquest.PlayQuestActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

import static com.kirich74.questsapp.data.ItemType.NO_SAVED_EMAIL;
import static java.security.AccessController.getContext;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class FirstLaunchActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            if (!Objects.equals(prefManager.getSavedEmail(), NO_SAVED_EMAIL)) {
                launchHomeScreen();
            }
            else {
                launchSignInActivity();
            }
            finish();
        }
        prefManager.setFirstTimeLaunch(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.first_launch_view_pager);
        viewPager.setAdapter(new ChooseFragmentAdapter(getSupportFragmentManager(),
                FirstLaunchActivity.this));
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(FirstLaunchActivity.this, MainActivity.class));
        finish();
    }

    private void launchSignInActivity() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(FirstLaunchActivity.this, SignInActivity.class));
        finish();
    }
}
