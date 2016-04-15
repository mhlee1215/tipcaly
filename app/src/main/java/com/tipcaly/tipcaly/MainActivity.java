
package com.tipcaly.tipcaly;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import android.support.design.widget.TabLayout;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;
    private boolean mGameIsInProgress;
    private long mTimerMilliseconds;

    boolean hasShownAd = true;
    private static final long GAME_LENGTH_MILLISECONDS = 10000;

    public static final String TAG_PAY_ALONE = "TAG_PAY_ALONE";
    public static final String TAG_PAY_TOGETHER = "TAG_PAY_TOGETHER";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();// getActionBar();
        actionBar.hide();
        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle("");

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between different app sections, select the corresponding tab.
//                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
//                // Tab.
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });
//
//        // For each of the sections in the app, add a tab to the action bar.
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            // Create a tab with text corresponding to the page title defined by the adapter.
//            // Also specify this Activity object, which implements the TabListener interface, as the
//            // listener for when this tab is selected.
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener(this));
//        }
//
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(mViewPager);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_perm_identity_black_48dp);
        tabLayout.getTabAt(0).setTag(TAG_PAY_ALONE);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_supervisor_account_black_48dp);
        tabLayout.getTabAt(1).setTag(TAG_PAY_TOGETHER);


        mViewPager.setPageTransformer(true, new DepthPageTransformer());


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if(TAG_PAY_ALONE.equals(tab.getTag())){
                    System.out.println("Hello111!");
                    List<String> bill = ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).getBill();
                    ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).setBillAmount(bill);
                    ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).updateExternalTipRatio();
                }else if(TAG_PAY_TOGETHER.equals(tab.getTag())){
                    System.out.println("Hello22!");
                    List<String> bill = ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).getBill();
                    ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).setBillAmount(bill);
                    ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).updateExternalTipRatio();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });








        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame();
            }
        });

        startGame();
    }

    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            hasShownAd = true;
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            startGame();
        }
    }

    public void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

        resumeGame(GAME_LENGTH_MILLISECONDS);
    }

    private void resumeGame(long milliseconds) {
        // Create a new timer for the correct length and start it.
        mGameIsInProgress = true;
        mTimerMilliseconds = milliseconds;
        createTimer(milliseconds);
        mCountDownTimer.start();
    }

    private void createTimer(final long milliseconds) {
        // Create the game timer, which counts down to the end of the level
        // and shows the "retry" button.
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

//        final TextView textView = ((TextView) findViewById(R.id.timer));

        mCountDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimerMilliseconds = millisUnitFinished;
//                textView.setText("seconds remaining: " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                mGameIsInProgress = false;
                if(!hasShownAd){
                    showInterstitial();

                }
//                textView.setText("done!");
//                mRetryButton.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public static SimpleTipCalculator simple;
        public static Fragment complex;

        static{
            simple = new SimpleTipCalculator();
            complex = new ComplexTipCalculator();
        }

        public AppSectionsPagerAdapter(FragmentManager fm) {

            super(fm);


        }

        public Fragment getFragment(int i){
            if(i == 0) return simple;
            else if(i == 1) return complex;
            else return null;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                   return simple;

                case 1:
                    return complex;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if( position == 0){
                return "Pay Alone";
            }else if(position == 1){
                return "Pay Together";
            }
            return "Null";
        }
    }







}
