
package com.tipcaly.tipcaly;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

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

    boolean hasShownAd = false;
    boolean isAdFree = true;
    private static final long GAME_LENGTH_MILLISECONDS = 20000;

    public static final String TAG_PAY_ALONE = "TAG_PAY_ALONE";
    public static final String TAG_PAY_TOGETHER = "TAG_PAY_TOGETHER";
    public static final String TAG_ABOUT_DEVELOPER = "TAG_ABOUT_DEVELOPER";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();// getActionBar();
        actionBar.hide();

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_head);
        tabLayout.setupWithViewPager(mViewPager);
        //tabLayout.setVisibility(View.GONE);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_black_48dp);
        tabLayout.getTabAt(0).setTag(TAG_PAY_ALONE);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_supervisor_account_black_48dp);
        tabLayout.getTabAt(1).setTag(TAG_PAY_TOGETHER);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_48dp);
        tabLayout.getTabAt(2).setTag(TAG_ABOUT_DEVELOPER);




        mViewPager.setPageTransformer(true, new DepthPageTransformer());


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("hi"+tab.getTag());
                mViewPager.setCurrentItem(tab.getPosition());
                if(TAG_PAY_ALONE.equals(tab.getTag())){
                    List<String> bill = ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).getBill();
                    ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).setBillAmount(bill);
                    ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).updateExternalTipRatio();
                }else if(TAG_PAY_TOGETHER.equals(tab.getTag())){
                    List<String> bill = ((SimpleTipCalculator)mAppSectionsPagerAdapter.getFragment(0)).getBill();
                    ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).setBillAmount(bill);
                    ((ComplexTipCalculator)mAppSectionsPagerAdapter.getFragment(1)).updateExternalTipRatio();
                }else if(TAG_ABOUT_DEVELOPER.equals(tab.getTag())){
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


        if(!isAdFree){
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

//    @Override
//    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
//        // When the given tab is selected, switch to the corresponding page in the ViewPager.
//        mViewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
//
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public static SimpleTipCalculator simple;
        public static Fragment complex;
        public static Fragment about;

        static{
            simple = new SimpleTipCalculator();
            complex = new ComplexTipCalculator();
            about = new AboutFragment();
        }

        public AppSectionsPagerAdapter(FragmentManager fm) {

            super(fm);


        }

        public Fragment getFragment(int i){
            if(i == 0) return simple;
            else if(i == 1) return complex;
            else if(i == 2) return about;
            else return null;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return simple;
                case 1:
                    return complex;
                case 2:
                    return about;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if( position == 0){
                return "Pay Alone";
            }else if(position == 1){
                return "Pay Together";
            }
            else if(position == 2){
                return "About";
            }
            return "Null";
        }
    }




}