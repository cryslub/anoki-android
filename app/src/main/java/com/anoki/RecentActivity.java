package com.anoki;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.ContactManage;

import java.util.Timer;
import java.util.TimerTask;


public class RecentActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        setTabHost();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ContactTask performBackgroundTask = new ContactTask(getContentResolver(),getApplicationContext());
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 600000); //execute in every 50000 ms



        //    mTabHost.addTab(mTabHost.newTabSpec("나").setIndicator("나").setContent(new Intent(this , MeTabActivity.class )));
//        mTabHost.setCurrentTab(0);


/*
        ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab1 = actionBar.newTab()
                .setText("최근")
                .setTabListener(new TabListener<RecentFragment>(
                        this, "최근", RecentFragment.class));
        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab()
                .setText("나")
                .setTabListener(new TabListener<MeFragment>(
                        this, "나", MeFragment.class));
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar.newTab()
                .setText("친구")
                .setTabListener(new TabListener<FriendsFragment>(
                        this, "친구", FriendsFragment.class));
        actionBar.addTab(tab3);

        ActionBar.Tab tab4 = actionBar.newTab()
                .setText("그룹")
                .setTabListener(new TabListener<GroupFragment>(
                        this, "그룹", GroupFragment.class));
        actionBar.addTab(tab4);

        ActionBar.Tab tab5 = actionBar.newTab()
                .setText("더보기")
                .setTabListener(new TabListener<MoreFragment>(
                        this, "더보기", MoreFragment.class));
        actionBar.addTab(tab5);
*/

    }


    private static class ContactTask extends AsyncTask<String, Void, String> {


        private ImageView view;
        private Bitmap bmp;
        ContentResolver contentResolver;
        Context applicationContext;

        public ContactTask(ContentResolver contentResolver, Context applicationContext) {
            this.contentResolver = contentResolver;
            this.applicationContext = applicationContext;
        }


        @Override
        protected String doInBackground(String... params) {
           // ContactManage.checkContact(contentResolver,applicationContext);
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void setTabHost(){

        final TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("최근").setIndicator("최근").setContent(new Intent(this, RecentTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("나").setIndicator("나").setContent(new Intent(this, MeTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("친구").setIndicator("친구").setContent(new Intent(this, FriendTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("그룹").setIndicator("그룹").setContent(new Intent(this, GroupTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("더보기").setIndicator("더보기").setContent(new Intent(this, MoreTabActivity.class)));


        for(int index = 0 ;index < mTabHost.getTabWidget().getChildCount() ; index++) {
            mTabHost.getTabWidget().getChildAt(index).setBackgroundColor(getResources().getColor(R.color.baltic_sea));
            mTabHost.getTabWidget().getChildAt(index).setPadding(0, 0, 0, 0);
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(index).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#aaaaaa"));
            tv.setTextSize(18);
        }

        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(Color.WHITE);


        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){

            @Override
            public void onTabChanged(String tabId) {

                for(int index = 0 ;index < mTabHost.getTabWidget().getChildCount() ; index++) {
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(index).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#aaaaaa"));
                }


                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
                tv.setTextColor(Color.WHITE);
            }
        });

    }


}
